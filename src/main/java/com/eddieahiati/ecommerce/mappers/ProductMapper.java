package com.eddieahiati.ecommerce.mappers;

import com.eddieahiati.ecommerce.dtos.ProductDto;
import com.eddieahiati.ecommerce.dtos.ProductRequest;
import com.eddieahiati.ecommerce.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "description", source = "category.name")
    ProductDto productDto(Product product);
    Product product(ProductDto productDto);
    @Mapping(target = "id", ignore = true)
    void update(ProductDto productDto, @MappingTarget Product product);
}
