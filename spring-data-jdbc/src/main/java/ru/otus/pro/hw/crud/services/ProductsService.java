package ru.otus.pro.hw.crud.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.pro.hw.crud.entities.Product;
import ru.otus.pro.hw.crud.exceptions.NotFoundException;
import ru.otus.pro.hw.crud.exceptions.ServiceException;
import ru.otus.pro.hw.crud.exceptions.ValidationException;
import ru.otus.pro.hw.crud.models.ProductCreateVM;
import ru.otus.pro.hw.crud.models.ProductItemVM;
import ru.otus.pro.hw.crud.repositories.ProductsRepository;

import java.util.List;

@Log4j2
@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public long create(ProductCreateVM productCreate) {
        log.debug("Create object", productCreate);

        validatePrice(productCreate.getPrice());
        try {
            var product = new Product();
            product.setTitle(productCreate.getTitle());
            product.setPrice(productCreate.getPrice());
            var result = productsRepository.save(product);
            return result.getId();
        } catch (Exception e) {
            throw new ServiceException("Ошибка сохранения продукта", e);
        }
    }

    public ProductItemVM getById(Long id) {
        var product = productsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %d not found", id)));
        return new ProductItemVM(product.getId(), product.getTitle(), product.getPrice());
    }

    public List<ProductItemVM> findAll() {
        return productsRepository.findAll()
                .stream()
                .map(p -> new ProductItemVM(p.getId(), p.getTitle(), p.getPrice())).toList();
    }

    @Transactional
    public void update(Long id, ProductCreateVM productUpdate) {
        log.debug(String.format("Update object %d", id), productUpdate);
        validatePrice(productUpdate.getPrice());
        var product = productsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %d not found", id)));

        product.setTitle(productUpdate.getTitle());
        product.setPrice(productUpdate.getPrice());
        productsRepository.save(product);
    }

    public void deleteById(Long id) {
        productsRepository.deleteById(id);
    }

    private void validatePrice(Integer price) {
        if (price < 5) {
            throw new ValidationException("The price cannot be lower than 5 rub.");
        }
    }

}
