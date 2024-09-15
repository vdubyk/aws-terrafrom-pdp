package org.avenga.service;

import lombok.RequiredArgsConstructor;
import org.avenga.exception.NotFoundException;
import org.avenga.model.Order;
import org.avenga.model.OrderProduct;
import org.avenga.model.Product;
import org.avenga.model.enumeration.OrderStatus;
import org.avenga.model.record.NewOrderProductRecord;
import org.avenga.model.record.NewOrderRecord;
import org.avenga.model.record.OrderProductRecord;
import org.avenga.model.record.OrderRecord;
import org.avenga.repository.OrderRepository;
import org.avenga.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final S3Service s3Service;

    private static final String BUCKET_NAME = "your-s3-bucket-name";

    public List<OrderRecord> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toOrderRecord)
                .collect(Collectors.toList());
    }

    public OrderRecord findById(Long id) {
        return orderRepository.findById(id)
                .map(this::toOrderRecord).orElseThrow(() -> new NotFoundException("Order not found with id " + id));
    }

    @Transactional
    public OrderRecord save(NewOrderRecord newOrderRecord) {
        var order = toOrderEntity(newOrderRecord);
        var savedOrder = orderRepository.save(order);
        return toOrderRecord(savedOrder);
    }

    public OrderRecord updateOrder(Long id, OrderRecord orderRecord) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id " + id));

        restoreProductQuantities(existingOrder.getOrderProducts());

        existingOrder.setOrderDate(orderRecord.orderDate());
        existingOrder.setContact(orderRecord.contact());
        existingOrder.setStatus(orderRecord.status());


        List<OrderProduct> updatedOrderProducts = orderRecord.orderProducts().stream()
                .map(this::toOrderProductEntity)
                .collect(Collectors.toList());

        existingOrder.setTotalPrice(calculateTotalPrice(updatedOrderProducts));

        reduceProductQuantities(updatedOrderProducts);

        existingOrder.setOrderProducts(updatedOrderProducts);

        Order savedOrder = orderRepository.save(existingOrder);

        return toOrderRecord(savedOrder);
    }


    private void restoreProductQuantities(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productRepository.findById(orderProduct.getProduct().getId())
                    .orElseThrow(() -> new NotFoundException("Product not found with id " + orderProduct.getProduct().getId()));

            product.setQuantity(product.getQuantity() + orderProduct.getQuantity());

            if (product.getQuantity() > 0) {
                product.setExist(true);
            }

            productRepository.save(product);
        }
    }

    private OrderProduct toOrderProductEntity(OrderProductRecord orderProductRecord) {
        Product product = productRepository.findById(orderProductRecord.productId())
                .orElseThrow(() -> new NotFoundException("Product not found with id " + orderProductRecord.productId()));

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(orderProductRecord.quantity());
        return orderProduct;
    }



    public String generateReceipt(OrderRecord orderRecord) {
        var receiptContent = createReceiptContent(orderRecord);
        var receiptKey = "receipts/order-" + orderRecord.id() + ".txt";
        return s3Service.uploadFile(BUCKET_NAME, receiptKey, receiptContent);
    }

    private String createReceiptContent(OrderRecord orderRecord) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Receipt for Order ID: ").append(orderRecord.id()).append("\n");
        receipt.append("Order Date: ").append(orderRecord.orderDate()).append("\n");
        receipt.append("Total Price: $").append(orderRecord.totalPrice()).append("\n");
        receipt.append("Status: ").append(orderRecord.status()).append("\n");
        receipt.append("Products: \n");

        orderRecord.orderProducts().forEach(product -> {
            receipt.append("- ").append(product.productId())
                    .append(" (Quantity: ").append(product.quantity()).append(")\n");
        });

        return receipt.toString();
    }

    private OrderRecord toOrderRecord(Order order) {
        var orderProducts = order.getOrderProducts().stream()
                .map(this::toOrderProductRecord)
                .collect(Collectors.toList());
        return new OrderRecord(order.getId(), order.getOrderDate(), order.getTotalPrice(), order.getContact(), order.getStatus(), orderProducts);
    }

    private OrderProductRecord toOrderProductRecord(OrderProduct orderProduct) {
        return new OrderProductRecord(orderProduct.getId(), orderProduct.getProduct().getId(), orderProduct.getQuantity());
    }

    private Order toOrderEntity(NewOrderRecord newOrderRecord) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setContact(newOrderRecord.contact());
        List<OrderProduct> orderProducts = newOrderRecord.products().stream()
                .map(orderProduct -> toOrderProductEntity(orderProduct, order))
                .collect(Collectors.toList());

        order.setTotalPrice(calculateTotalPrice(orderProducts));

        order.setOrderProducts(orderProducts);

        reduceProductQuantities(orderProducts);

        return order;
    }

    private void reduceProductQuantities(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productRepository.findById(orderProduct.getProduct().getId())
                    .orElseThrow(() -> new NotFoundException("Product not found with id " + orderProduct.getProduct().getId()));

            if (product.getQuantity() < orderProduct.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - orderProduct.getQuantity());

            if (product.getQuantity() == 0) {
                product.setExist(false);
            }

            productRepository.save(product);
        }
    }

    private OrderProduct toOrderProductEntity(NewOrderProductRecord newOrderProductRecord, Order order) {
        var product = new Product();
        product.setId(newOrderProductRecord.productId());

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setOrder(order);
        orderProduct.setQuantity(newOrderProductRecord.quantity());
        return orderProduct;
    }

    private double calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .mapToDouble(orderProduct -> {
                    Product product = productRepository.findById(orderProduct.getProduct().getId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    return product.getPrice() * orderProduct.getQuantity();
                })
                .sum();
    }
}


