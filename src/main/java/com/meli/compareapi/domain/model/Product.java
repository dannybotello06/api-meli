package com.meli.compareapi.domain.model;



import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class Product {
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private BigDecimal price;
    private Double rating;
    private Map<String, String> specs;

    public Product() {}

    public Product(String id, String name, String imageUrl, String description,
                   BigDecimal price, Double rating, Map<String, String> specs) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.specs = specs;
    }



}
