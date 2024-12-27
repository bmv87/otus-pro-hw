package ru.otus.pro.hw.di;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CartImpl implements Cart {
    private static final Logger logger = LoggerFactory.getLogger(CartImpl.class);

    public CartImpl(Repository<Product> repository) {
        this.repository = repository;
    }

    @Autowired
    private Repository<Product> repository;
    private Map<Integer, List<Product>> items = new HashMap<>();

    @Override
    public void add(int id) {
        try {
            var product = repository.get(id);
            if (items.containsKey(id)) {
                var item = new ArrayList<>(items.get(id));
                item.add(product);
                items.put(id, item);
            } else {
                items.put(id, List.of(product));
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(int id) {
        items.remove(id);
    }

    @Override
    public List<String> view() {
        return items.entrySet().stream()
                .map(i -> String.format("Product id: %d name: %s count: %d", i.getKey(), i.getValue().getFirst().getName(), i.getValue().size()))
                .toList();
    }
}
