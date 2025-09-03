package com.meli.compareapi.application.mapper;

import com.meli.compareapi.domain.model.Product;
import com.meli.compareapi.infrastructure.ProductEntity;
import com.meli.compareapi.infrastructure.ProductWsDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductWsDto toDto(Product product) {
        return new ProductWsDto(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getDescription(),
                product.getPrice(),
                product.getRating(),
                product.getSpecs()
        );
    }

    public Product toDomain(ProductEntity dto) {
        return new Product(
                dto.getId(),
                dto.getName(),
                dto.getImageUrl(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getRating(),
                dto.getSpecs()
        );
    }
}