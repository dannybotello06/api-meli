package com.meli.compareapi.application.services;

import com.meli.compareapi.domain.model.PageResult;
import com.meli.compareapi.domain.model.Product;
import com.meli.compareapi.domain.port.in.ProductUseCase;
import com.meli.compareapi.infrastructure.CompareRequest;
import com.meli.compareapi.infrastructure.ProductWsDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductService  {


    private final ProductUseCase productUseCase;

    public ProductService(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }


    public PageResult<ProductWsDto> getAllProduct(int page, int size) {
        return productUseCase.getAllProduct( page,  size);
    }

    public  Product findById(String id) {

        return productUseCase.findById(id);

    }

    public List<Product>  compareByIds (CompareRequest request) {

        return productUseCase.compareByIds(request);

    }


}
