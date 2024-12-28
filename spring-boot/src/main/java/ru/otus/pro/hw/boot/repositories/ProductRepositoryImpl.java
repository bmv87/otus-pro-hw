package ru.otus.pro.hw.boot.repositories;

import org.springframework.stereotype.Component;
import ru.otus.pro.hw.boot.repositories.entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component("productRepository")
public class ProductRepositoryImpl implements Repository<Product> {

    private final AtomicLong keyCounter = new AtomicLong(0);
    private final List<Product> productsStore = new ArrayList<>();

    @Override
    public void addRange(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("products is null");
        }
        products.forEach(this::add);
    }

    @Override
    public long add(Product product) {
        if (productsStore.stream().anyMatch(p -> p.getId() == product.getId())) {
            throw new RuntimeException("Unique key constraint");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new RuntimeException("Name is required");
        }
        product.setId(keyCounter.incrementAndGet());

        productsStore.add(product);
        return product.getId();
    }

    @Override
    public Optional<Product> get(long id) {
        return productsStore.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public List<Product> getAll() {
        return productsStore;
    }
}