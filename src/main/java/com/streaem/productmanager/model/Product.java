package com.streaem.productmanager.model;

import com.streaem.productmanager.dto.ProductRequestDTO;
import com.streaem.productmanager.restclient.dto.ProductDTO;
import com.streaem.productmanager.util.Utils;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Product {

    private String name;
    private String category;
    private BigDecimal price;
    private String description;
    private Integer stockLevel;

    public Product(ProductDTO productDTO) {
        if (productDTO == null) {
            return;
        }
        this.name = Utils.getValue(productDTO.getName());
        this.category = Utils.getValue(productDTO.getCategory());
        this.price = Utils.getValue(productDTO.getPrice());
        this.description = productDTO.getDescription();
        this.stockLevel = 0;
    }

    public Product(ProductRequestDTO productDTO) {
        if (productDTO == null) {
            return;
        }
        this.name = productDTO.getName();
        this.category = productDTO.getCategory();
        this.price = productDTO.getPrice();
        this.description = productDTO.getDescription();
        this.stockLevel = productDTO.getStockLevel();
    }

}
