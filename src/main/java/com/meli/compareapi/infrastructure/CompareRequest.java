package com.meli.compareapi.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
public class CompareRequest {

    private List<String> ids;
    private String sortBy;
    private String direction;
}
