package ru.otus.pro.hw.di;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("productRepository")
public class ProductRepositoryImpl implements Repository<Product> {

    private List<Product> productsStore = new ArrayList<>();

    @Override
    public void addRange(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("products is null");
        }
        products.forEach(this::add);
    }

    @Override
    public void add(Product product) {
        if (productsStore.stream().anyMatch(p -> p.getId() == product.getId())) {
            throw new RuntimeException("Unique key constraint");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new RuntimeException("Name is required");
        }
        productsStore.add(product);
    }

    @Override
    public Product get(int id) {
        return productsStore.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Product with id %d not found", id)));
    }

    @Override
    public List<Product> getAll() {
        return productsStore;
    }
}
