package com.streaem.productmanager.restclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductDTO {

    private String name;
    private String category;
    private BigDecimal price;
    private String description;

}
