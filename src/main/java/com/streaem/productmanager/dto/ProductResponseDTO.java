package com.streaem.productmanager.dto;

import com.streaem.productmanager.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDTO {

    private String name;
    private String category;
    private BigDecimal price;
    private String description;
    private Integer stockLevel;

    public ProductResponseDTO(Product product) {
        if (product == null) {
            return;
        }
        this.name = product.getName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.stockLevel = product.getStockLevel();
    }
}
