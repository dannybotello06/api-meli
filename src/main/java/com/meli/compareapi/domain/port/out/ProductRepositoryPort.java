package com.meli.compareapi.domain.port.out;

import com.meli.compareapi.domain.model.Product;
import com.meli.compareapi.infrastructure.ProductEntity;
import com.meli.compareapi.infrastructure.ProductWsDto;

import java.util.List;

public interface ProductRepositoryPort {

    List<Product> findAll();
    Product findById(String id);
    List<Product> findAllById(List<String> ids);
}
