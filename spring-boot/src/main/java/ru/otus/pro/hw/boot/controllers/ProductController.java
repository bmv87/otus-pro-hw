package ru.otus.pro.hw.boot.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.pro.hw.boot.models.ProductCreateVM;
import ru.otus.pro.hw.boot.models.ProductVM;
import ru.otus.pro.hw.boot.services.ProductServiceImpl;

import java.util.List;

@RestController
public class ProductController {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<ProductVM> getAllProduct() {
        return productService.getAll();
    }

    @GetMapping("/products/{id}")
    public ProductVM getById(@PathVariable(name = "id") Long id) {
        return productService.getById(id);
    }

    @PostMapping("/products")
    public void create(@RequestBody ProductCreateVM product) {
        productService.create(product);
    }
}
