package ru.otus.pro.hw.boot.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void addRange(List<T> products);

    long add(T product);

    Optional<T> get(long id);

    List<T> getAll();
}