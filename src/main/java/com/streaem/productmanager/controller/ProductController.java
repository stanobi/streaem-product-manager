package com.streaem.productmanager.controller;

import com.streaem.productmanager.dto.ProductRequestDTO;
import com.streaem.productmanager.dto.ProductResponseDTO;
import com.streaem.productmanager.dto.ResponseDTO;
import com.streaem.productmanager.dto.StockLevelRequestDTO;
import com.streaem.productmanager.dto.SuccessResponseDTO;
import com.streaem.productmanager.exception.StreaemException;
import com.streaem.productmanager.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories/{category}/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllProduct(@PathVariable String category,
                                                     @RequestParam(required = false, name = "instock") Boolean inStock) throws StreaemException {
        List<ProductResponseDTO> allProductByCategory = productService.getAllProductByCategory(category, inStock);
        return ResponseEntity.ok(new SuccessResponseDTO(allProductByCategory));
    }

    @GetMapping("/{productName}")
    public ResponseEntity<ResponseDTO> getProduct(@PathVariable String category,
                                                  @PathVariable String productName) throws StreaemException {
        ProductResponseDTO productResponseDTO = productService.getProduct(category, productName);
        return ResponseEntity.ok(new SuccessResponseDTO(productResponseDTO));
    }

    @PutMapping("/{productName}")
    public ResponseEntity<ResponseDTO> updateProduct(@PathVariable String category,
                                                     @PathVariable String productName,
                                                     @Valid @RequestBody ProductRequestDTO request) throws StreaemException {
        ProductResponseDTO productResponseDTO = productService.updateProduct(category, productName, request);
        return ResponseEntity.ok(new SuccessResponseDTO(productResponseDTO));
    }

    @PatchMapping("/{productName}")
    public ResponseEntity<ResponseDTO> updateStockLevel(@PathVariable String category,
                                                        @PathVariable String productName,
                                                        @Valid @RequestBody StockLevelRequestDTO request) throws StreaemException {
        ProductResponseDTO productResponseDTO = productService.updateProductStockLevel(category, productName, request);
        return ResponseEntity.ok(new SuccessResponseDTO(productResponseDTO));
    }

}
