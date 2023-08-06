package com.streaem.productmanager.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDTO {

    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "invalid name provided, should not contain special character.")
    @Size(max = 50, message = "invalid name, maximum length is 50")
    @NotBlank(message = "name is required")
    private String name;

    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "invalid category provided, should not contain special character.")
    @Size(max = 50, message = "invalid category, maximum length is 50")
    @NotBlank(message = "category is required")
    private String category;

    @DecimalMin(value = "0.01", message = "price must be greater than zero")
    @Digits(integer = 1000000000, fraction = 2)
    @NotNull(message = "price is required")
    private BigDecimal price;

    @NotBlank(message = "description is required")
    private String description;

    @Min(value = 1L, message = "stockLevel must be greater than or equal to zero" )
    @Digits(integer = 1000000000, fraction = 0)
    private Integer stockLevel;

}
