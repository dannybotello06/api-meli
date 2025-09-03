package com.meli.compareapi.domain.port.in;


import com.meli.compareapi.domain.model.PageResult;
import com.meli.compareapi.domain.model.Product;
import com.meli.compareapi.infrastructure.CompareRequest;
import com.meli.compareapi.infrastructure.ProductWsDto;

import java.util.List;

public interface ProductUseCase {

    PageResult<ProductWsDto> getAllProduct(int page, int size);
    Product findById(String id);
    List<Product> compareByIds(CompareRequest request);
}
