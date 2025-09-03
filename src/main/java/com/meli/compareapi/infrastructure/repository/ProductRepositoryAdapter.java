package com.meli.compareapi.infrastructure.repository;


import com.meli.compareapi.application.mapper.ProductMapper;
import com.meli.compareapi.domain.model.Product;
import com.meli.compareapi.domain.port.out.ProductRepositoryPort;
import com.meli.compareapi.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {


    private final FileRepository repository;
    private final ProductMapper mapper;

    public ProductRepositoryAdapter(FileRepository repository, ProductMapper mapper){
        this.repository=repository;
        this.mapper = mapper;
    }

    @Override
    public List<Product> findAll() {

      return   repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Product findById(String id) {
        return mapper.toDomain( repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product %s not found".formatted(id))));

    }

    @Override
    public List<Product> findAllById(List<String> ids) {
        return   repository.findAllById(ids).stream().map(mapper::toDomain).toList();

    }


}
