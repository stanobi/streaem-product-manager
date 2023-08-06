package com.streaem.productmanager.service;

import com.streaem.productmanager.dto.ProductRequestDTO;
import com.streaem.productmanager.dto.StockLevelRequestDTO;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceInvalidInputTest {

    @Autowired
    private ProductService productService;

    @Test
    void given_invalidData_when_getAllProductByCategory_should_throwException() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.getAllProductByCategory(null, null));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.getAllProductByCategory("", null));
    }

    @Test
    void given_invalidData_when_getProduct_should_throwException() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.getProduct(null, null));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.getProduct("", ""));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.getProduct("sampleCategory", ""));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.getProduct("sampleCategory", null));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.getProduct("", "sampleProduct"));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.getProduct(null, "sampleProduct"));
    }

    @Test
    void given_invalidData_when_updateProduct_should_throwException() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProduct(null, null, null));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProduct("sampleCategory", "sampleProduct", null));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProduct("sampleCategory", "", null));
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProduct("sampleCategory", "", productRequestDTO));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProduct("sampleCategory", null, productRequestDTO));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProduct("", "sampleProduct", productRequestDTO));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProduct(null, "sampleProduct", productRequestDTO));
    }

    @Test
    void given_invalidData_when_updateProductStockLevel_should_throwException() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProductStockLevel(null, null, null));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProductStockLevel("sampleCategory", "sampleProduct", null));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProductStockLevel("sampleCategory", "", null));
        StockLevelRequestDTO stockLevelRequestDTO = new StockLevelRequestDTO();
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProductStockLevel("sampleCategory", "", stockLevelRequestDTO));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProductStockLevel("sampleCategory", null, stockLevelRequestDTO));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProductStockLevel("", "sampleProduct", stockLevelRequestDTO));
        Assertions.assertThrows(ConstraintViolationException.class, () -> productService.updateProductStockLevel(null, "sampleProduct", stockLevelRequestDTO));
    }

}