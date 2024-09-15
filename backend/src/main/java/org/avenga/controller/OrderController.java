package org.avenga.controller;

import lombok.RequiredArgsConstructor;
import org.avenga.model.record.NewOrderRecord;
import org.avenga.model.record.OrderRecord;
import org.avenga.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderRecord> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public OrderRecord getOrderById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @PostMapping
    public OrderRecord createOrder(@RequestBody NewOrderRecord newOrderRecord) {
        return orderService.save(newOrderRecord);
    }

    @PutMapping("/{id}")
    public OrderRecord updateOrder(@PathVariable Long id, @RequestBody OrderRecord orderRecord) {
        return orderService.updateOrder(id, orderRecord);
    }

    @PostMapping("/{id}/generate-receipt")
    public String generateReceipt(@PathVariable Long id) {
        OrderRecord orderRecord = orderService.findById(id);
        return orderService.generateReceipt(orderRecord);
    }
}
