package ru.otus.pro.hw.di;

import java.util.List;

public interface Repository<T> {
    void addRange(List<T> products);

    void add(T product);

    T get(int id);

    List<T> getAll();
}
