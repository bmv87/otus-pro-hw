package ru.otus.pro.hw.crud.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.pro.hw.crud.entities.Product;

@Repository
public interface ProductsRepository extends ListCrudRepository<Product, Long> {
}
