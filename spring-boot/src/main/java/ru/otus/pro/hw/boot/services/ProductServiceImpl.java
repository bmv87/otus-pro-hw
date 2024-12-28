package ru.otus.pro.hw.boot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.pro.hw.boot.exceptions.NotFoundException;
import ru.otus.pro.hw.boot.exceptions.ServiceException;
import ru.otus.pro.hw.boot.mappers.ProductMapper;
import ru.otus.pro.hw.boot.models.ProductCreateVM;
import ru.otus.pro.hw.boot.models.ProductVM;
import ru.otus.pro.hw.boot.repositories.Repository;
import ru.otus.pro.hw.boot.repositories.entities.Product;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final Repository<Product> productRepository;

    @Override
    public void create(ProductCreateVM productCreateVM) {
        try {
            log.debug(productCreateVM);
            var product = ProductMapper.INSTANCE.dtoToEntity(productCreateVM);
            productRepository.add(product);
        } catch (RuntimeException e) {
            throw new ServiceException("Product saving error", e);
        }
    }

    @Override
    public ProductVM getById(long id) {
        log.debug("id = " + id);
        var product = productRepository.get(id)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %d not found", id)));
        log.debug(product);
        return ProductMapper.INSTANCE.entityToDto(product);
    }

    @Override
    public List<ProductVM> getAll() {
        return ProductMapper.INSTANCE.mapListToDto(productRepository.getAll());
    }
}
