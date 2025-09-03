package com.meli.compareapi.application.usecases;


import com.meli.compareapi.application.mapper.ProductMapper;
import com.meli.compareapi.domain.model.PageResult;
import com.meli.compareapi.domain.model.Product;
import com.meli.compareapi.domain.port.in.ProductUseCase;
import com.meli.compareapi.domain.port.out.ProductRepositoryPort;
import com.meli.compareapi.infrastructure.CompareRequest;
import com.meli.compareapi.infrastructure.ProductWsDto;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component

public class ProductUseCaseImpl implements ProductUseCase {

 private  final ProductRepositoryPort repositoryPort;

 private ProductMapper mapper;

    public ProductUseCaseImpl(ProductRepositoryPort repositoryPort, ProductMapper mapper) {
        this.repositoryPort = repositoryPort;
        this.mapper = mapper;
    }


    @Override
    public PageResult<ProductWsDto> getAllProduct(int page, int size) {
        if (page < 0) throw new IllegalArgumentException("page debe ser >= 0");
        if (size <= 0) throw new IllegalArgumentException("size debe ser > 0");

        List<ProductWsDto> all =  repositoryPort.findAll().stream().map(mapper::toDto).toList();
        long totalElements = all.size();
        int fromIndex = page * size;
        List<ProductWsDto> content;

        if (fromIndex >= all.size()) {
            content = Collections.emptyList();
        } else {
            int toIndex = Math.min(fromIndex + size, all.size());
            content = all.subList(fromIndex, toIndex);
        }



        int totalPages = (int) ((totalElements + size - 1) / size);

        return PageResult.<ProductWsDto>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();



    }
    @Override
    public Product findById(String id){

        return  repositoryPort.findById(id);
    }


    @Override
    public List<Product> compareByIds (CompareRequest request) {

        List<Product> products = repositoryPort.findAllById(request.getIds());

        if ("price".equalsIgnoreCase(request.getSortBy())) {
            Comparator<Product> cmp = Comparator.comparing(Product::getPrice, Comparator.nullsLast(Comparator.naturalOrder()));
            if ("desc".equalsIgnoreCase(request.getDirection())) cmp = cmp.reversed();
            products = products.stream().sorted(cmp).collect(Collectors.toList());
        } else if ("rating".equalsIgnoreCase(request.getSortBy())) {
            Comparator<Product> cmp = Comparator.comparing(Product::getRating, Comparator.nullsLast(Comparator.naturalOrder()));
            if ("desc".equalsIgnoreCase(request.getDirection())) cmp = cmp.reversed();
            products = products.stream().sorted(cmp).collect(Collectors.toList());
        }

        return products;
    }


}
