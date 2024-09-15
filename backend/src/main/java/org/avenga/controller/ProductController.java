package org.avenga.controller;

import lombok.RequiredArgsConstructor;
import org.avenga.model.Product;
import org.avenga.model.record.NewProductRecord;
import org.avenga.model.record.ProductRecord;
import org.avenga.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductRecord> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductRecord getProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public ProductRecord createProduct(@RequestBody NewProductRecord newProductRecord) {
        return productService.save(newProductRecord);
    }

    @PutMapping("/{id}")
    public ProductRecord updateProduct(@PathVariable Long id, @RequestBody NewProductRecord newProductRecord) {
        return productService.update(id, newProductRecord);
    }
}