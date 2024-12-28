package ru.otus.pro.hw.persistence;

import java.io.Serializable;
import java.util.Optional;

public interface Repository<T, ID extends Serializable> {
    T add(T entity);

    Optional<T> get(ID id);
}
