package com.streaem.productmanager.repo;

import com.streaem.productmanager.dto.ProductRequestDTO;
import com.streaem.productmanager.dto.ProductResponseDTO;
import com.streaem.productmanager.dto.StockLevelRequestDTO;
import com.streaem.productmanager.exception.StreaemException;
import com.streaem.productmanager.exception.StreaemRuntimeException;
import com.streaem.productmanager.model.Product;
import com.streaem.productmanager.restclient.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Repository
public class ProductRepository {

    private static Map<String, Map<String, Product>> productData = new HashMap<>();

    public void persistDataFromApi(List<ProductDTO> productDTOList) throws StreaemRuntimeException {
        productData.clear();
        if (productDTOList == null || productDTOList.isEmpty()) {
            log.error("no product provided");
            throw new StreaemRuntimeException("No product provided");
        }

        for (ProductDTO productDTO : productDTOList) {
            Product product = new Product(productDTO);
            Map<String, Product> productMapByCategory = productData.getOrDefault(product.getCategory(), new HashMap<>());
            if (productMapByCategory.isEmpty()) {
                productMapByCategory.put(product.getName(), product);
                productData.put(product.getCategory(), productMapByCategory);
            } else {
                productMapByCategory.put(product.getName(), product);
            }
        }
    }

    public Set<String> getProductCategories() {
        return productData.keySet();
    }

    public List<ProductResponseDTO> getProductsByCategory(String category) throws StreaemException {
        Map<String, Product> productMap = getProductMap(category);
        return productMap.values().stream().map(ProductResponseDTO::new).toList();
    }

    public List<ProductResponseDTO> getProductsByCategoryAndInStock(String category, boolean inStock) throws StreaemException {
        Map<String, Product> productMap = getProductMap(category);
        if (inStock) {
            return productMap.values().stream().map(ProductResponseDTO::new)
                    .filter(productResponseDTO -> productResponseDTO.getStockLevel() > 0)
                    .toList();
        }
        return productMap.values().stream().map(ProductResponseDTO::new)
                .filter(productResponseDTO -> productResponseDTO.getStockLevel() == 0)
                .toList();
    }

    public ProductResponseDTO getProduct(String category, String productName) throws StreaemException {
        Product product = getProductData(category, productName);
        return new ProductResponseDTO(product);
    }

    public ProductResponseDTO updateProduct(String category, String productName,
                                            ProductRequestDTO requestDTO) throws StreaemException {
        updateProductData(category, productName, requestDTO);
        Product product = getProductData(requestDTO.getCategory(), requestDTO.getName());
        return new ProductResponseDTO(product);
    }

    public ProductResponseDTO updateProductStockLevel(String category, String productName,
                                                      StockLevelRequestDTO requestDTO) throws StreaemException {
        Product product = getProductData(category, productName);
        product.setStockLevel(requestDTO.getStockLevel());
        return new ProductResponseDTO(product);
    }

    private void updateProductData(String category, String currentProductName,
                                           ProductRequestDTO requestDTO) throws StreaemException {

        Map<String, Product> productMap = validateExistingData(category, currentProductName);

        if (category.equals(requestDTO.getCategory())) {
            updateProductInSameCategory(productMap, currentProductName, requestDTO);
            return;
        }

        Map<String, Product> productMapToBeUpdated = productData.getOrDefault(requestDTO.getCategory(), new HashMap<>());
        if (productMapToBeUpdated.isEmpty()) {
            //category never existed before now.
            productMapToBeUpdated.put(requestDTO.getName(), new Product(requestDTO));
            productData.put(requestDTO.getCategory(), productMapToBeUpdated);
            productMap.remove(currentProductName);
            return;
        }

        moveProductIntoAnotherCategory(productMap, productMapToBeUpdated, currentProductName, requestDTO);
    }

    private void moveProductIntoAnotherCategory(Map<String, Product> currentProductMap, Map<String, Product> productMap,
                                                String currentProductName, ProductRequestDTO requestDTO) throws StreaemException {

        if (productMap.containsKey(requestDTO.getName())) {
            log.error("new product name : {} already exist in the new category name : {}", requestDTO.getName(), requestDTO.getCategory());
            throw new StreaemException(HttpStatus.BAD_REQUEST, "new product name provided already exist in new category provided");
        }

        currentProductMap.remove(currentProductName);
        productMap.put(requestDTO.getName(), new Product(requestDTO));
    }

    private void updateProductInSameCategory(Map<String, Product> productMap, String currentProductName,
                               ProductRequestDTO requestDTO) throws StreaemException {
        if (!currentProductName.equals(requestDTO.getName())) {
            if (productMap.containsKey(requestDTO.getName())) {
                log.error("new product name : {} already exist", requestDTO.getName());
                throw new StreaemException(HttpStatus.BAD_REQUEST, "new product name provided already exist");
            } else {
                productMap.remove(currentProductName);
                productMap.put(requestDTO.getName(), new Product(requestDTO));
                return;
            }
        }

        Product product = productMap.get(currentProductName);
        product.setCategory(requestDTO.getCategory());
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStockLevel(requestDTO.getStockLevel());
    }

    private Map<String, Product> validateExistingData(String category,
                                                      String productName) throws StreaemException {
        Map<String, Product> productMap = getProductMap(category);
        if (!productMap.containsKey(productName)) {
            log.error("unknown product name : {} provided", productName);
            throw new StreaemException(HttpStatus.NOT_FOUND, "unknown product name provided");
        }
        return productMap;
    }

    private Product getProductData(String category, String productName) throws StreaemException {
        Map<String, Product> productMap = getProductMap(category);
        Product product = productMap.get(productName);
        if (product == null) {
            log.error("Unable to process request due to unknown productName provided : {}", productName);
            throw new StreaemException(HttpStatus.NOT_FOUND, "Unknown product name provided");
        }
        return product;
    }

    private Map<String, Product> getProductMap(String category) throws StreaemException {
        Map<String, Product> productMap = productData.getOrDefault(category, new HashMap<>());
        if (productMap.isEmpty()) {
            log.error("Unable to process request due to unknown category provided : {}", category);
            throw new StreaemException(HttpStatus.NOT_FOUND, "Unknown category provided");
        }
        return productMap;
    }

}
