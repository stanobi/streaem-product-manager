package com.streaem.productmanager.service;

import com.streaem.productmanager.dto.ProductRequestDTO;
import com.streaem.productmanager.dto.ProductResponseDTO;
import com.streaem.productmanager.dto.StockLevelRequestDTO;
import com.streaem.productmanager.exception.StreaemException;
import com.streaem.productmanager.exception.StreaemRuntimeException;
import com.streaem.productmanager.model.Product;
import com.streaem.productmanager.repo.ProductRepository;
import com.streaem.productmanager.restclient.ProductAPIClient;
import com.streaem.productmanager.restclient.dto.ProductDTO;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductAPIClient productAPIClient;

    @InjectMocks
    private ProductService productService;

    @Test
    void given_validDataAndAPIFetchIsSuccessful_when_populateDatasource_should_populateDatasource() {
        List<ProductDTO> productDTOList = List.of(
                new ProductDTO("Sample Product Name", "SampleCategoryName", BigDecimal.ONE, "SampleDescription"),
                new ProductDTO("Sample Product Name2", "SampleCategoryName", BigDecimal.TEN, "SampleDescription"),
                new ProductDTO("Sample Product Name2", "SampleCategoryName2", BigDecimal.ONE, "SampleDescription")
        );
        Mockito.when(productAPIClient.getProductData()).thenReturn(productDTOList);
        Assertions.assertDoesNotThrow(() -> productService.populateDatasource());
    }

    @Test
    void given_validDataAndAPIFetchFailed_when_populateDatasource_should_throwRuntimeException() {
        Mockito.when(productAPIClient.getProductData()).thenThrow(new FeignException.FeignServerException(500, "Exception Occurred",
                Request.create(Request.HttpMethod.GET, "http://sampleUrl:4001", new HashMap<>(), Request.Body.create(""), new RequestTemplate()),
                null, null));
        Assertions.assertThrows(StreaemRuntimeException.class, () -> productService.populateDatasource());
    }

    @Test
    void given_validData_when_getAllProductCategories_should_returnAllCategories() {
        Mockito.when(productRepository.getProductCategories()).thenReturn(Set.of("SampleCategoryName", "SampleCategoryName2"));
        Set<String> categories = Assertions.assertDoesNotThrow(() -> productService.getAllProductCategories());
        Assertions.assertEquals(2, categories.size());
    }

    @Test
    void given_validDataAndInStockAsNull_when_getAllProductByCategory_should_returnAllProductInACategory() throws StreaemException {
        Mockito.when(productRepository.getProductsByCategory(Mockito.anyString())).thenReturn(List.of(
                getSampleProductResponseDTO("sampleCategoryName", "sampleProductName", "Sample Description", BigDecimal.ONE, 2),
                getSampleProductResponseDTO("sampleCategoryName", "sampleProductName2", "Sample Description", BigDecimal.ONE, 0),
                getSampleProductResponseDTO("sampleCategoryName", "sampleProductName3", "Sample Description", BigDecimal.ONE, 0)
        ));
        List<ProductResponseDTO> productResponseDTOList = Assertions.assertDoesNotThrow(() -> productService.getAllProductByCategory("sampleCategoryName", null));
        Assertions.assertEquals(3, productResponseDTOList.size());
    }

    @Test
    void given_validDataAndInStockAsTrue_when_getAllProductByCategory_should_returnAllInStockProductInACategory() throws StreaemException {
        Mockito.when(productRepository.getProductsByCategoryAndInStock(Mockito.anyString(), Mockito.eq(Boolean.TRUE))).thenReturn(List.of(
                getSampleProductResponseDTO("sampleCategoryName", "sampleProductName", "Sample Description", BigDecimal.ONE, 2)
        ));
        List<ProductResponseDTO> productResponseDTOList = Assertions.assertDoesNotThrow(() -> productService.getAllProductByCategory("sampleCategoryName", true));
        Assertions.assertEquals(1, productResponseDTOList.size());
    }

    @Test
    void given_validDataAndInStockAsFalse_when_getAllProductByCategory_should_returnAllOutOfStockProductInACategory() throws StreaemException {
        Mockito.when(productRepository.getProductsByCategoryAndInStock(Mockito.anyString(), Mockito.eq(Boolean.FALSE))).thenReturn(List.of(
                getSampleProductResponseDTO("sampleCategoryName", "sampleProductName2", "Sample Description", BigDecimal.ONE, 0),
                getSampleProductResponseDTO("sampleCategoryName", "sampleProductName3", "Sample Description", BigDecimal.ONE, 0)
        ));
        List<ProductResponseDTO> productResponseDTOList = Assertions.assertDoesNotThrow(() -> productService.getAllProductByCategory("sampleCategoryName", false));
        Assertions.assertEquals(2, productResponseDTOList.size());
    }

    @Test
    void given_validData_when_getProduct_should_returnProductInformation() throws StreaemException {
        Mockito.when(productRepository.getProduct(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(getSampleProductResponseDTO("sampleCategoryName", "sampleProductName2", "Sample Description", BigDecimal.ONE, 0));
        ProductResponseDTO productResponseDTO = productService.getProduct("sampleCategoryName", "sampleProductName2");
        Assertions.assertNotNull(productResponseDTO);
        Assertions.assertEquals("sampleCategoryName", productResponseDTO.getCategory());
        Assertions.assertEquals("sampleProductName2", productResponseDTO.getName());
        Assertions.assertEquals("Sample Description", productResponseDTO.getDescription());
        Assertions.assertEquals(BigDecimal.ONE, productResponseDTO.getPrice());
        Assertions.assertEquals(0, productResponseDTO.getStockLevel());
    }

    @Test
    void given_validData_when_updateProduct_should_returnUpdatedProductInformation() throws StreaemException {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setName("NewProductName");
        requestDTO.setCategory("NewCategoryName");
        requestDTO.setPrice(BigDecimal.ONE);
        requestDTO.setDescription("Sample Description");
        requestDTO.setStockLevel(12);
        Mockito.when(productRepository.updateProduct(Mockito.anyString(), Mockito.anyString(), Mockito.any(ProductRequestDTO.class)))
                .thenReturn(getSampleProductResponseDTO(requestDTO.getCategory(),
                requestDTO.getName(), requestDTO.getDescription(), requestDTO.getPrice(), requestDTO.getStockLevel()));
        ProductResponseDTO productResponseDTO = productService.updateProduct("sampleCategoryName", "sampleProductName2", requestDTO);
        Assertions.assertNotNull(productResponseDTO);
        Assertions.assertEquals(requestDTO.getCategory(), productResponseDTO.getCategory());
        Assertions.assertEquals(requestDTO.getName(), productResponseDTO.getName());
        Assertions.assertEquals(requestDTO.getDescription(), productResponseDTO.getDescription());
        Assertions.assertEquals(requestDTO.getPrice(), productResponseDTO.getPrice());
        Assertions.assertEquals(requestDTO.getStockLevel(), productResponseDTO.getStockLevel());
    }

    @Test
    void given_validData_when_updateProductStockLevel_should_returnUpdatedProductInformation() throws StreaemException {
        StockLevelRequestDTO requestDTO = new StockLevelRequestDTO();
        requestDTO.setStockLevel(12);
        Mockito.when(productRepository.updateProductStockLevel(Mockito.anyString(), Mockito.anyString(), Mockito.any(StockLevelRequestDTO.class)))
                .thenReturn(getSampleProductResponseDTO("sampleCategoryName", "sampleProductName2",
                        "Sample Description", BigDecimal.ONE, requestDTO.getStockLevel()));
        ProductResponseDTO productResponseDTO = productService.updateProductStockLevel("sampleCategoryName", "sampleProductName2", requestDTO);
        Assertions.assertNotNull(productResponseDTO);
        Assertions.assertEquals("sampleCategoryName", productResponseDTO.getCategory());
        Assertions.assertEquals("sampleProductName2", productResponseDTO.getName());
        Assertions.assertEquals("Sample Description", productResponseDTO.getDescription());
        Assertions.assertEquals(BigDecimal.ONE, productResponseDTO.getPrice());
        Assertions.assertEquals(requestDTO.getStockLevel(), productResponseDTO.getStockLevel());
    }

    private ProductResponseDTO getSampleProductResponseDTO(String category, String productName,
                                                           String description, BigDecimal price,
                                                           int stockLevel) {
        ProductDTO productDTO = new ProductDTO(productName, category, price, description);
        Product product = new Product(productDTO);
        product.setStockLevel(stockLevel);
        return new ProductResponseDTO(product);
    }
}