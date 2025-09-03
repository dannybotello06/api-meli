package com.meli.compareapi.application.services;


import com.meli.compareapi.domain.model.PageResult;
import com.meli.compareapi.domain.model.Product;
import com.meli.compareapi.domain.port.in.ProductUseCase;
import com.meli.compareapi.infrastructure.CompareRequest;
import com.meli.compareapi.infrastructure.ProductWsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ProductServicesTest {

    @Mock
    private ProductUseCase productUseCase;

    @InjectMocks
    private ProductService service;


    private Product product;
    private ProductWsDto productWsDto;
    private CompareRequest compareRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId("1");
        product.setName("Product 1");

        productWsDto = new ProductWsDto();
        productWsDto.setId("1");
        productWsDto.setName("Product 1");
        List<String> idsToCompare = List.of("MLA1", "MLA2");
        compareRequest = new CompareRequest(idsToCompare,"price","desc");

    }

    @Test
    void getAllProductReturnPageResult() {
        //arrange
        int page=0;
        int size=2;
        List<ProductWsDto> productList = List.of(new ProductWsDto(), new ProductWsDto());
        PageResult<ProductWsDto> expectedResult = new PageResult<>(productList, page, size, 2L,1);
        when(productUseCase.getAllProduct(page, size)).thenReturn(expectedResult);
        // action
        PageResult<ProductWsDto> result = service.getAllProduct(page, size);
        // assert
        assertEquals(expectedResult, result);
        verify(productUseCase, times(1)).getAllProduct(page, size);
    }
    @Test
    void testCompareByIds() {

        when(productUseCase.compareByIds(compareRequest)).thenReturn(Collections.singletonList(product));
        List<Product> result = service.compareByIds(compareRequest);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product 1", result.get(0).getName());
        verify(productUseCase).compareByIds(compareRequest);
    }

    @Test
    void testFindById() {

        when(productUseCase.findById("1")).thenReturn(product);
        Product result = service.findById("1");
        assertNotNull(result);
        assertEquals("Product 1", result.getName());
        verify(productUseCase).findById("1");
    }
}


