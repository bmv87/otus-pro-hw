package ru.otus.pro.hw.crud.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.otus.pro.hw.crud.models.ProductCreateVM;
import ru.otus.pro.hw.crud.models.ProductItemVM;
import ru.otus.pro.hw.crud.services.ProductsService;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public List<ProductItemVM> getAllProducts() {
        return productsService.findAll();
    }

    @GetMapping("/{id}")
    public ProductItemVM getProductById(@PathVariable Long id) {
        return productsService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productsService.deleteById(id);
    }

    @PostMapping()
    public long create(@Valid @RequestBody ProductCreateVM productCreate) {
        return productsService.create(productCreate);
    }

    @PutMapping("/{id}")
    public void create(@PathVariable Long id, @Valid @RequestBody ProductCreateVM productCreate) {
        productsService.update(id, productCreate);
    }
}