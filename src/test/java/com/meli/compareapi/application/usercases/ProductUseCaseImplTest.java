package com.meli.compareapi.application.usercases;

import com.meli.compareapi.application.mapper.ProductMapper;
import com.meli.compareapi.application.usecases.ProductUseCaseImpl;
import com.meli.compareapi.domain.model.PageResult;
import com.meli.compareapi.domain.model.Product;
import com.meli.compareapi.domain.port.out.ProductRepositoryPort;
import com.meli.compareapi.infrastructure.CompareRequest;
import com.meli.compareapi.infrastructure.ProductWsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductUseCaseImplTest {
    @Mock
    private ProductRepositoryPort repositoryPort;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductUseCaseImpl productUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProduct_ValidPagination() {
        // Arrange
        Product product = new Product("1", "Product 1","ulr","Description1", BigDecimal.TEN, 4.5,null);
        ProductWsDto dto = new ProductWsDto("1", "Product 1","ulr","Description1", BigDecimal.TEN, 4.5,null);

        when(repositoryPort.findAll()).thenReturn(List.of(product));
        when(mapper.toDto(product)).thenReturn(dto);

        // Act
        PageResult<ProductWsDto> result = productUseCase.getAllProduct(0, 1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("1", result.getContent().get(0).getId());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetAllProduct_InvalidPage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productUseCase.getAllProduct(-1, 5);
        });
        assertEquals("page debe ser >= 0", exception.getMessage());
    }

    @Test
    void testGetAllProduct_InvalidSize() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productUseCase.getAllProduct(0, 0);
        });
        assertEquals("size debe ser > 0", exception.getMessage());
    }

    @Test
    void testFindById_ReturnsProduct() {
        Product product = new Product("1", "Product 1","ulr","Description1", BigDecimal.TEN, 4D,null);
        when(repositoryPort.findById("1")).thenReturn(product);

        Product result = productUseCase.findById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Product 1", result.getName());
    }

    @Test
    void testCompareByIds_SortByPriceAsc() {
        Product p1 = new Product("1", "Product 1","ulr","Description1", new BigDecimal("10.00"), 4.0D,null);
        Product p2 = new Product("2", "Product 2","ulr","Description2", new BigDecimal("20.00"), 4.5D,null);
        CompareRequest request = new CompareRequest(List.of("1", "2"),"price","asc");
        when(repositoryPort.findAllById(List.of("1", "2"))).thenReturn(List.of(p2, p1));

        List<Product> result = productUseCase.compareByIds(request);

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
    }

    @Test
    void testCompareByIds_SortByRatingDesc() {
        Product p1 = new Product("1", "Product 1","ulr","Description1", BigDecimal.TEN, 3.0,null);
        Product p2 = new Product("2", "Product 2","ulr","Description2", BigDecimal.ONE, 5.0,null);
        CompareRequest request = new CompareRequest(List.of("1", "2"),"rating","desc");

        when(repositoryPort.findAllById(List.of("1", "2"))).thenReturn(List.of(p1, p2));

        List<Product> result = productUseCase.compareByIds(request);

        assertEquals("2", result.get(0).getId());
        assertEquals("1", result.get(1).getId());
    }

    @Test
    void testCompareByIds_NoSorting() {
        Product p1 = new Product("1", "Product 1","ulr","Description1", BigDecimal.TEN, 3.0,null);
        Product p2 = new Product("2", "Product 2","ulr","Description2", BigDecimal.ONE, 5.0,null);
        CompareRequest request = new CompareRequest(List.of("1", "2"),"other","asc");

        when(repositoryPort.findAllById(List.of("1", "2"))).thenReturn(List.of(p1, p2));

        List<Product> result = productUseCase.compareByIds(request);


        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
    }

}
