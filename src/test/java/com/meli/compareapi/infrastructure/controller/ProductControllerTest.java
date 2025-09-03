package com.meli.compareapi.infrastructure.controller;

import com.meli.compareapi.application.mapper.ProductMapper;
import com.meli.compareapi.application.services.ProductService;
import com.meli.compareapi.domain.model.PageResult;
import com.meli.compareapi.domain.model.Product;
import com.meli.compareapi.infrastructure.CompareRequest;
import com.meli.compareapi.infrastructure.ProductWsDto;
import com.meli.compareapi.infrastructure.controllers.ProductController;
import com.meli.compareapi.infrastructure.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {


    @Mock
    private ProductService productService;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductController productController;

    @Nested
    @DisplayName("Tests for getAllProducts endpoint")
    class GetAllProductsTests {

        @Test
        @DisplayName("Should return OK with a PageResult when page and size are valid")
        void givenValidPageAndSize_whenGetAllProducts_thenReturnsOkWithPageResult() {
            // Arrange (Preparación)
            int page = 0;
            int size = 10;
            PageResult<ProductWsDto> expectedPageResult = new PageResult<>(Collections.emptyList(),0,0,0L,0);
            when(productService.getAllProduct(page, size)).thenReturn(expectedPageResult);
            // Act
            ResponseEntity<?> response = productController.getAllProducts(page, size);
            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(expectedPageResult, response.getBody());
            verify(productService, times(1)).getAllProduct(page, size);
        }
        @Test
        @DisplayName("Should throw BadRequestException when page is negative")
        void givenNegativePage_whenGetAllProducts_thenThrowsBadRequestException() {
            // Arrange
            int negativePage = -1;
            int validSize = 10;

            // Act & Assert
            // Verificamos que se lanza la excepción esperada y capturamos su mensaje.
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                productController.getAllProducts(negativePage, validSize);
            });

            assertEquals("page should be  >= 0", exception.getMessage());

            // Verificamos que el servicio NUNCA fue llamado, ya que la validación falló antes.
            verify(productService, never()).getAllProduct(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Should throw BadRequestException when size is zero")
        void givenZeroSize_whenGetAllProducts_thenThrowsBadRequestException() {
            // Arrange
            int validPage = 0;
            int zeroSize = 0;

            // Act & Assert
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                productController.getAllProducts(validPage, zeroSize);
            });

            assertEquals("size should be between 1 y 100", exception.getMessage());
            verify(productService, never()).getAllProduct(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Should throw BadRequestException when size is greater than 100")
        void givenSizeTooLarge_whenGetAllProducts_thenThrowsBadRequestException() {
            // Arrange
            int validPage = 0;
            int largeSize = 101;

            // Act & Assert
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                productController.getAllProducts(validPage, largeSize);
            });

            assertEquals("size should be between 1 y 100", exception.getMessage());
            verify(productService, never()).getAllProduct(anyInt(), anyInt());
        }
    }

    @Nested
    @DisplayName("Tests for getById endpoint")
    class GetByIdTests {

        @Test
        @DisplayName("Should return a ProductWsDto when a valid ID is provided")
        void givenValidId_whenGetById_thenReturnsProductWsDto() {
            // Arrange
            String productId = "MLA123";
            Product domainProduct = new Product(); // Objeto de dominio simulado
            domainProduct.setId(productId);
            ProductWsDto expectedDto = new ProductWsDto(); // DTO simulado
            expectedDto.setId(productId);

            when(productService.findById(productId)).thenReturn(domainProduct);
            when(productMapper.toDto(domainProduct)).thenReturn(expectedDto);

            // Act
            ProductWsDto actualDto = productController.getById(productId);

            // Assert
            assertNotNull(actualDto);
            assertEquals(expectedDto.getId(), actualDto.getId());

            // Verificamos las interacciones con los mocks
            verify(productService, times(1)).findById(productId);
            verify(productMapper, times(1)).toDto(domainProduct);
        }
    }

    @Nested
    @DisplayName("Tests for compare endpoint")
    class CompareTests {

        @Test
        @DisplayName("Should return a list of ProductWsDto when a valid CompareRequest is provided")
        void givenCompareRequest_whenCompare_thenReturnsListOfProductWsDto() {
            // Arrange
            List<String> idsToCompare = List.of("MLA1", "MLA2");
            CompareRequest request = new CompareRequest(idsToCompare,"price","desc");

            Product product1 = new Product();
            product1.setId("MLA1");
            product1.setPrice(BigDecimal.valueOf(154545D));
            Product product2 = new Product();
            product2.setId("MLA2");
            product1.setPrice(BigDecimal.valueOf(15005D));
            List<Product> domainProducts = List.of(product1, product2);

            ProductWsDto dto1 = new ProductWsDto();
            dto1.setId("MLA1");
            ProductWsDto dto2 = new ProductWsDto();
            dto2.setId("MLA2");
            List<ProductWsDto> expectedDtos = List.of(dto1, dto2);

            when(productService.compareByIds(request)).thenReturn(domainProducts);
            when(productMapper.toDto(product1)).thenReturn(dto1);
            when(productMapper.toDto(product2)).thenReturn(dto2);

            // Act
            List<ProductWsDto> actualDtos = productController.compare(request);

            // Assert
            assertNotNull(actualDtos);
            assertEquals(2, actualDtos.size());
            assertEquals(expectedDtos, actualDtos);

            // Verificamos interacciones
            verify(productService, times(1)).compareByIds(request);
            verify(productMapper, times(1)).toDto(product1);
            verify(productMapper, times(1)).toDto(product2);
        }
    }

}




