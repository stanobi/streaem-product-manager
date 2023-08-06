package com.streaem.productmanager.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockLevelRequestDTO {

    @Min(value = 1L, message = "stockLevel must be greater than or equal to zero" )
    @Digits(integer = 1000000000, fraction = 0)
    @NotNull(message = "stockLevel is required")
    private Integer stockLevel;
    
}
