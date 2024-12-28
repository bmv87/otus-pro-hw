package ru.otus.pro.hw.boot.services;

import ru.otus.pro.hw.boot.models.ProductCreateVM;
import ru.otus.pro.hw.boot.models.ProductVM;

import java.util.List;

public interface ProductService {

    void create(ProductCreateVM product);

    ProductVM getById(long id);

    List<ProductVM> getAll();
}
