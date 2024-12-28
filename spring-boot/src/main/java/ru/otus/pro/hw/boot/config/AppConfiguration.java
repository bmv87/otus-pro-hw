package ru.otus.pro.hw.boot.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ru.otus.pro.hw.boot.repositories.Repository;
import ru.otus.pro.hw.boot.repositories.entities.Product;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfiguration {
    @Autowired
    Repository<Product> repository;


    @PostConstruct
    public void fillProducts() {

        var list = new ArrayList<>(List.of(
                new Product(1, "Product 1", 3),
                new Product(2, "Product 21", 4),
                new Product(3, "Product 3", 2),
                new Product(4, "Product 4", 8)
        ));
        repository.addRange(list);
    }
}
