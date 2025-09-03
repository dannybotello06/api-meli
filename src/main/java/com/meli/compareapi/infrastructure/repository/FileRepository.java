package com.meli.compareapi.infrastructure.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.meli.compareapi.infrastructure.ProductEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class FileRepository {


    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final String fileLocation;
    private final AtomicReference<List<ProductEntity>> cache = new AtomicReference<>();

    public FileRepository(ResourceLoader rl, ObjectMapper om,
                                 @Value("${app.products.file:classpath:data/products.json}") String fileLocation) {
        this.resourceLoader = rl;
        this.objectMapper = om;
        this.fileLocation = fileLocation;
    }

    private List<ProductEntity> loadAll() {
        List<ProductEntity> cached = cache.get();
        if (cached != null) return cached;

        try {
            Resource res = resourceLoader.getResource(fileLocation); // soporta classpath: y file:
            try (InputStream is = res.getInputStream()) {
                List<ProductEntity> list = objectMapper.readValue(is, new TypeReference<List<ProductEntity>>() {});
                cache.compareAndSet(null, Collections.unmodifiableList(list));
                return cache.get();
            }
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo leer productos desde: " + fileLocation, e);
        }
    }

    public List<ProductEntity> findAll() { return loadAll(); }

    public Optional<ProductEntity> findById(String id) {
        return  loadAll().stream().filter(e-> e.getId().equals(id)).findFirst();

    }
    public List<ProductEntity> findAllById(List<String> ids) {

        return loadAll().stream().filter( p-> ids.contains(p.getId())).collect(Collectors.toList()); }

}
