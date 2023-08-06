package com.streaem.productmanager.repo;

import com.streaem.productmanager.dto.ProductRequestDTO;
import com.streaem.productmanager.dto.ProductResponseDTO;
import com.streaem.productmanager.dto.StockLevelRequestDTO;
import com.streaem.productmanager.exception.StreaemException;
import com.streaem.productmanager.exception.StreaemRuntimeException;
import com.streaem.productmanager.model.Product;
import com.streaem.productmanager.restclient.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    void given_validProductList_when_persistDataFromApi_should_storeDataSuccessfully() {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        Map<String, Map<String, Product>> productData = (Map<String, Map<String, Product>>) ReflectionTestUtils.getField(ProductRepository.class, "productData");
        Assertions.assertFalse(productData.isEmpty());
        Assertions.assertEquals(2, productData.keySet().size());
    }

    @Test
    void given_emptyProductList_when_persistDataFromApi_should_throwException() {
        Assertions.assertThrows(StreaemRuntimeException.class, () -> productRepository.persistDataFromApi(null));
        Assertions.assertThrows(StreaemRuntimeException.class, () -> productRepository.persistDataFromApi(new ArrayList<>()));
    }

    @Test
    void given_validData_when_getProductCategories_should_returnCategories() {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        Set<String> productCategories = productRepository.getProductCategories();
        Assertions.assertFalse(productCategories.isEmpty());
        Assertions.assertEquals(2, productCategories.size());
    }

    @Test
    void given_validData_when_getProductsByCategory_should_returnProducts() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        List<ProductResponseDTO> productList = productRepository.getProductsByCategory("SampleCategoryName");
        Assertions.assertEquals(2, productList.size());

        productList = productRepository.getProductsByCategory("SampleCategoryName2");
        Assertions.assertEquals(1, productList.size());
    }

    @Test
    void given_unknownCategory_when_getProductsByCategory_should_throwException() {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        StreaemException streaemException = Assertions.assertThrows(StreaemException.class, () -> productRepository.getProductsByCategory("anotherCategory"));
        Assertions.assertNotNull(streaemException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, streaemException.getHttpStatus());
        Assertions.assertEquals("Unknown category provided", streaemException.getMessage());
    }

    @Test
    void given_validData_when_getProductsByCategoryAndInStock_should_returnProducts() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        List<ProductResponseDTO> productList = productRepository.getProductsByCategoryAndInStock("SampleCategoryName", false);
        Assertions.assertEquals(2, productList.size());

        productList = productRepository.getProductsByCategoryAndInStock("SampleCategoryName", true);
        Assertions.assertEquals(0, productList.size());

        productList = productRepository.getProductsByCategoryAndInStock("SampleCategoryName2", false);
        Assertions.assertEquals(1, productList.size());

        productList = productRepository.getProductsByCategoryAndInStock("SampleCategoryName2", true);
        Assertions.assertEquals(0, productList.size());
    }

    @Test
    void given_unknownCategory_when_getProductsByCategoryAndInStock_should_throwException() {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        Assertions.assertThrows(StreaemException.class, () -> productRepository.getProductsByCategoryAndInStock("anotherCategory", false));
        Assertions.assertThrows(StreaemException.class, () -> productRepository.getProductsByCategoryAndInStock("anotherCategory", true));
    }

    @Test
    void given_validData_when_getProduct_should_returnProductInformation() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        ProductResponseDTO product = productRepository.getProduct("SampleCategoryName", "SampleProductName");
        Assertions.assertNotNull(product);
        Assertions.assertEquals("SampleProductName", product.getName());
        Assertions.assertEquals("SampleCategoryName", product.getCategory());
        Assertions.assertEquals("Sample Description", product.getDescription());
        Assertions.assertEquals(BigDecimal.ONE, product.getPrice());
        Assertions.assertEquals(0, product.getStockLevel());
    }

    @Test
    void given_unknownProductName_when_getProduct_should_throwException() {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        StreaemException streaemException = Assertions.assertThrows(StreaemException.class, () -> productRepository.getProduct("SampleCategoryName", "AnotherProductName"));
        Assertions.assertNotNull(streaemException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, streaemException.getHttpStatus());
        Assertions.assertEquals("Unknown product name provided", streaemException.getMessage());
    }

    @Test
    void given_validDataToUpdateToSameCategoryAndUnknownProduct_when_updateProduct_should_throwException() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCategory("SampleCategoryName");
        requestDTO.setName("SampleProductName");
        requestDTO.setDescription("NewDescription");
        requestDTO.setPrice(new BigDecimal("12.00"));
        requestDTO.setStockLevel(23);
        StreaemException streaemException = Assertions.assertThrows(StreaemException.class, () -> productRepository.updateProduct("SampleCategoryName", "UnknownProductName", requestDTO));
        Assertions.assertNotNull(streaemException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, streaemException.getHttpStatus());
        Assertions.assertEquals("unknown product name provided", streaemException.getMessage());
    }

    @Test
    void given_validDataToUpdateToSameCategoryAndAlreadyExistingProduct_when_updateProduct_should_throwException() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCategory("SampleCategoryName");
        requestDTO.setName("SampleProductName2");
        requestDTO.setDescription("NewDescription");
        requestDTO.setPrice(new BigDecimal("12.00"));
        requestDTO.setStockLevel(23);
        StreaemException streaemException = Assertions.assertThrows(StreaemException.class, () -> productRepository.updateProduct("SampleCategoryName", "SampleProductName", requestDTO));
        Assertions.assertNotNull(streaemException);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, streaemException.getHttpStatus());
        Assertions.assertEquals("new product name provided already exist", streaemException.getMessage());
    }

    @Test
    void given_validDataToUpdateToSameCategoryAndSameProduct_when_updateProduct_should_returnUpdatedProductInformation() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCategory("SampleCategoryName");
        requestDTO.setName("SampleProductName");
        requestDTO.setDescription("NewDescription");
        requestDTO.setPrice(new BigDecimal("12.00"));
        requestDTO.setStockLevel(23);
        ProductResponseDTO updatedProduct = productRepository.updateProduct("SampleCategoryName", "SampleProductName", requestDTO);
        Assertions.assertNotNull(updatedProduct);

        ProductResponseDTO product = productRepository.getProduct("SampleCategoryName", "SampleProductName");
        Assertions.assertNotNull(product);
        Assertions.assertEquals("SampleProductName", product.getName());
        Assertions.assertEquals("SampleCategoryName", product.getCategory());
        Assertions.assertEquals("NewDescription", product.getDescription());
        Assertions.assertEquals(new BigDecimal("12.00"), product.getPrice());
        Assertions.assertEquals(23, product.getStockLevel());
    }

    @Test
    void given_validDataToUpdateToSameCategoryAndDifferentProduct_when_updateProduct_should_returnUpdatedProductInformation() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCategory("SampleCategoryName");
        requestDTO.setName("NewProductName");
        requestDTO.setDescription("NewDescription");
        requestDTO.setPrice(new BigDecimal("12.00"));
        requestDTO.setStockLevel(23);
        ProductResponseDTO updatedProduct = productRepository.updateProduct("SampleCategoryName", "SampleProductName", requestDTO);
        Assertions.assertNotNull(updatedProduct);

        StreaemException streaemException = Assertions.assertThrows(StreaemException.class, () -> productRepository.getProduct("SampleCategoryName", "SampleProductName"));
        Assertions.assertNotNull(streaemException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, streaemException.getHttpStatus());
        Assertions.assertEquals("Unknown product name provided", streaemException.getMessage());

        ProductResponseDTO product = productRepository.getProduct("SampleCategoryName", "NewProductName");
        Assertions.assertNotNull(product);
        Assertions.assertEquals("NewProductName", product.getName());
        Assertions.assertEquals("SampleCategoryName", product.getCategory());
        Assertions.assertEquals("NewDescription", product.getDescription());
        Assertions.assertEquals(new BigDecimal("12.00"), product.getPrice());
        Assertions.assertEquals(23, product.getStockLevel());
    }

    @Test
    void given_validDataToUpdateToDifferentCategoryAndSameProduct_when_updateProduct_should_returnUpdatedProductInformation() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCategory("NewCategoryName");
        requestDTO.setName("SampleProductName");
        requestDTO.setDescription("NewDescription");
        requestDTO.setPrice(new BigDecimal("12.00"));
        requestDTO.setStockLevel(23);
        ProductResponseDTO updatedProduct = productRepository.updateProduct("SampleCategoryName", "SampleProductName", requestDTO);
        Assertions.assertNotNull(updatedProduct);

        StreaemException streaemException = Assertions.assertThrows(StreaemException.class, () -> productRepository.getProduct("SampleCategoryName", "SampleProductName"));
        Assertions.assertNotNull(streaemException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, streaemException.getHttpStatus());
        Assertions.assertEquals("Unknown product name provided", streaemException.getMessage());

        ProductResponseDTO product = productRepository.getProduct("NewCategoryName", "SampleProductName");
        Assertions.assertNotNull(product);
        Assertions.assertEquals("SampleProductName", product.getName());
        Assertions.assertEquals("NewCategoryName", product.getCategory());
        Assertions.assertEquals("NewDescription", product.getDescription());
        Assertions.assertEquals(new BigDecimal("12.00"), product.getPrice());
        Assertions.assertEquals(23, product.getStockLevel());
    }

    @Test
    void given_validDataToUpdateToDifferentCategoryAndDifferentProduct_when_updateProduct_should_returnUpdatedProductInformation() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCategory("NewCategoryName");
        requestDTO.setName("NewProductName");
        requestDTO.setDescription("NewDescription");
        requestDTO.setPrice(new BigDecimal("12.00"));
        requestDTO.setStockLevel(23);
        ProductResponseDTO updatedProduct = productRepository.updateProduct("SampleCategoryName", "SampleProductName", requestDTO);
        Assertions.assertNotNull(updatedProduct);

        StreaemException streaemException = Assertions.assertThrows(StreaemException.class, () -> productRepository.getProduct("SampleCategoryName", "SampleProductName"));
        Assertions.assertNotNull(streaemException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, streaemException.getHttpStatus());
        Assertions.assertEquals("Unknown product name provided", streaemException.getMessage());

        ProductResponseDTO product = productRepository.getProduct("NewCategoryName", "NewProductName");
        Assertions.assertNotNull(product);
        Assertions.assertEquals("NewProductName", product.getName());
        Assertions.assertEquals("NewCategoryName", product.getCategory());
        Assertions.assertEquals("NewDescription", product.getDescription());
        Assertions.assertEquals(new BigDecimal("12.00"), product.getPrice());
        Assertions.assertEquals(23, product.getStockLevel());
    }

    @Test
    void given_validDataToUpdateToExistingCategoryAndDifferentProduct_when_updateProduct_should_returnUpdatedProductInformation() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCategory("SampleCategoryName2");
        requestDTO.setName("SampleProductName2");
        requestDTO.setDescription("NewDescription");
        requestDTO.setPrice(new BigDecimal("12.00"));
        requestDTO.setStockLevel(23);
        ProductResponseDTO updatedProduct = productRepository.updateProduct("SampleCategoryName", "SampleProductName", requestDTO);
        Assertions.assertNotNull(updatedProduct);

        StreaemException streaemException = Assertions.assertThrows(StreaemException.class, () -> productRepository.getProduct("SampleCategoryName", "SampleProductName"));
        Assertions.assertNotNull(streaemException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, streaemException.getHttpStatus());
        Assertions.assertEquals("Unknown product name provided", streaemException.getMessage());

        ProductResponseDTO product = productRepository.getProduct("SampleCategoryName2", "SampleProductName2");
        Assertions.assertNotNull(product);
        Assertions.assertEquals("SampleProductName2", product.getName());
        Assertions.assertEquals("SampleCategoryName2", product.getCategory());
        Assertions.assertEquals("NewDescription", product.getDescription());
        Assertions.assertEquals(new BigDecimal("12.00"), product.getPrice());
        Assertions.assertEquals(23, product.getStockLevel());
    }

    @Test
    void given_validDataToUpdateToExistingCategoryAndExistingProduct_when_updateProduct_should_throwException() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCategory("SampleCategoryName2");
        requestDTO.setName("SampleProductName");
        requestDTO.setDescription("NewDescription");
        requestDTO.setPrice(new BigDecimal("12.00"));
        requestDTO.setStockLevel(23);
        StreaemException streaemException = Assertions.assertThrows(StreaemException.class, () -> productRepository.updateProduct("SampleCategoryName", "SampleProductName", requestDTO));
        Assertions.assertNotNull(streaemException);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, streaemException.getHttpStatus());
        Assertions.assertEquals("new product name provided already exist in new category provided", streaemException.getMessage());

        //test to confirm data was not updated
        ProductResponseDTO product = productRepository.getProduct("SampleCategoryName", "SampleProductName");
        Assertions.assertNotNull(product);
        Assertions.assertEquals("SampleProductName", product.getName());
        Assertions.assertEquals("SampleCategoryName", product.getCategory());
        Assertions.assertEquals("Sample Description", product.getDescription());
        Assertions.assertEquals(BigDecimal.ONE, product.getPrice());
        Assertions.assertEquals(0, product.getStockLevel());
    }

    @Test
    void given_validData_when_updateProductStockLevel_should_returnUpdatedProductInformation() throws StreaemException {
        Assertions.assertDoesNotThrow(() -> productRepository.persistDataFromApi(getSampleProducts()));
        StockLevelRequestDTO requestDTO = new StockLevelRequestDTO();
        requestDTO.setStockLevel(23);
        ProductResponseDTO updatedProduct = productRepository.updateProductStockLevel("SampleCategoryName", "SampleProductName", requestDTO);
        Assertions.assertNotNull(updatedProduct);
        Assertions.assertEquals(23, updatedProduct.getStockLevel());

        ProductResponseDTO product = productRepository.getProduct("SampleCategoryName", "SampleProductName");
        Assertions.assertNotNull(product);
        Assertions.assertEquals("SampleProductName", product.getName());
        Assertions.assertEquals("SampleCategoryName", product.getCategory());
        Assertions.assertEquals("Sample Description", product.getDescription());
        Assertions.assertEquals(BigDecimal.ONE, product.getPrice());
        Assertions.assertEquals(23, product.getStockLevel());
    }

    private List<ProductDTO> getSampleProducts(){
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(new ProductDTO("SampleProductName", "SampleCategoryName", BigDecimal.ONE, "Sample Description"));
        productDTOList.add(new ProductDTO("SampleProductName2", "SampleCategoryName", BigDecimal.TEN, "Sample Sample Description"));
        productDTOList.add(new ProductDTO("SampleProductName", "SampleCategoryName2", BigDecimal.ZERO, "Sample Sample Description"));
        return productDTOList;
    }
}