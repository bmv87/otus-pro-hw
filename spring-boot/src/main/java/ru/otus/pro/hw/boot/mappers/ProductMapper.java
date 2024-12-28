package ru.otus.pro.hw.boot.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.otus.pro.hw.boot.models.ProductCreateVM;
import ru.otus.pro.hw.boot.models.ProductVM;
import ru.otus.pro.hw.boot.repositories.entities.Product;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", ignore = true)
    Product dtoToEntity(ProductCreateVM productCreateVM);

    ProductVM entityToDto(Product product);

    List<ProductVM> mapListToDto(List<Product> entities);
}
