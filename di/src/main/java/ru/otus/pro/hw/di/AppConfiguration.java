package ru.otus.pro.hw.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfiguration {
    @Autowired
    Repository<Product> repository;

    @Bean()
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Cart cart(@Autowired Repository<Product> repository) {
        return new CartImpl(repository);
    }

    @PostConstruct
    public void fillProducts() {

        var list = new ArrayList<Product>(List.of(
                new Product(1, "Product 1", 3),
                new Product(2, "Product 21", 4),
                new Product(3, "Product 3", 2),
                new Product(4, "Product 4", 8)
        ));
        repository.addRange(list);
    }
}
