package com.meli.compareapi.infrastructure.controllers;

import com.meli.compareapi.application.mapper.ProductMapper;
import com.meli.compareapi.application.services.ProductService;
import com.meli.compareapi.domain.model.PageResult;
import com.meli.compareapi.infrastructure.CompareRequest;
import com.meli.compareapi.infrastructure.ProductWsDto;
import com.meli.compareapi.infrastructure.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;

    private final ProductMapper mapper;

    public ProductController(ProductService service, ProductMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }


    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        if (page < 0)   throw new BadRequestException("page should be  >= 0");
        if (size <= 0 || size > 100)   throw new BadRequestException("size should be between 1 y 100");
        PageResult<ProductWsDto> result= service.getAllProduct(page,size);
        return  ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ProductWsDto  getById(@PathVariable String id) {
        return  mapper.toDto( service.findById(id));
    }

    @PostMapping("/compare")
    public List<ProductWsDto> compare(@RequestBody CompareRequest req) {
        return  service.compareByIds(req).stream().map(mapper::toDto).toList();
    }



}
