package com.streaem.productmanager.service;

import com.streaem.productmanager.dto.ProductRequestDTO;
import com.streaem.productmanager.dto.ProductResponseDTO;
import com.streaem.productmanager.dto.StockLevelRequestDTO;
import com.streaem.productmanager.exception.StreaemException;
import com.streaem.productmanager.exception.StreaemRuntimeException;
import com.streaem.productmanager.repo.ProductRepository;
import com.streaem.productmanager.restclient.ProductAPIClient;
import com.streaem.productmanager.restclient.dto.ProductDTO;
import feign.FeignException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductAPIClient productAPIClient;


    public void populateDatasource() throws StreaemRuntimeException {
        List<ProductDTO> productDTOList = fetchProductViaApi();
        productRepository.persistDataFromApi(productDTOList);
    }

    public Set<String> getAllProductCategories() {
        return productRepository.getProductCategories();
    }

    public List<ProductResponseDTO> getAllProductByCategory(@NotBlank String category, Boolean inStock) throws StreaemException {
        if (inStock == null) {
            return productRepository.getProductsByCategory(category);
        }
        return productRepository.getProductsByCategoryAndInStock(category, inStock);
    }

    public ProductResponseDTO getProduct(@NotBlank String category, @NotBlank String productName) throws StreaemException {
        return productRepository.getProduct(category, productName);
    }

    public ProductResponseDTO updateProduct(@NotBlank String category, @NotBlank String productName,
                                            @NotNull ProductRequestDTO productRequestDTO) throws StreaemException {
        return productRepository.updateProduct(category, productName, productRequestDTO);
    }

    public ProductResponseDTO updateProductStockLevel(@NotBlank String category, @NotBlank String productName,
                                                      @NotNull StockLevelRequestDTO stockLevelRequestDTO) throws StreaemException {
        return productRepository.updateProductStockLevel(category, productName, stockLevelRequestDTO);
    }

    private List<ProductDTO> fetchProductViaApi() throws StreaemRuntimeException {
        try {
            return productAPIClient.getProductData();
        } catch (FeignException fex) {
            throw new StreaemRuntimeException("Unable to fetch Products via API", fex);
        }
    }

}
