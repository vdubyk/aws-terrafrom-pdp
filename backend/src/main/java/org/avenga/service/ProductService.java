package org.avenga.service;

import lombok.RequiredArgsConstructor;
import org.avenga.exception.NotFoundException;
import org.avenga.model.Product;
import org.avenga.model.record.NewProductRecord;
import org.avenga.model.record.ProductRecord;
import org.avenga.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductRecord> findAll() {
        return productRepository.findAll().stream()
                .map(this::toProductRecord)
                .collect(Collectors.toList());
    }

    public ProductRecord findById(Long id) {
        return productRepository.findById(id)
                .map(this::toProductRecord).orElseThrow(()-> new NotFoundException("Product not found with id " + id));
    }

    public ProductRecord save(NewProductRecord newProductRecord) {
        Product product = toProductEntity(newProductRecord);

        Product savedProduct = productRepository.save(product);
        return toProductRecord(savedProduct);
    }

    public ProductRecord update(Long id, NewProductRecord newProductRecord) {
        Product product = toProductEntity(newProductRecord);
        product.setId(id);
        product.setQuantity(newProductRecord.quantity());
        if(product.getQuantity()==0) {
            product.setExist(false);
        } else {
            product.setExist(true);
        }
        Product updatedProduct = productRepository.save(product);
        return toProductRecord(updatedProduct);
    }

    private ProductRecord toProductRecord(Product product) {
        return new ProductRecord(product.getId(), product.getName(), product.getPrice(), product.getQuantity(), product.isExist());
    }

    private Product toProductEntity(NewProductRecord newProductRecord) {
        var product = new Product();
        product.setName(newProductRecord.name());
        product.setPrice(newProductRecord.price());

        var quantity = newProductRecord.quantity() != null ? newProductRecord.quantity() : 0;
        product.setQuantity(quantity);
        product.setExist(quantity != 0);
        return product;
    }
}

