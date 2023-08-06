package com.streaem.productmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaem.productmanager.dto.ProductRequestDTO;
import com.streaem.productmanager.dto.ProductResponseDTO;
import com.streaem.productmanager.dto.ResponseDTO;
import com.streaem.productmanager.dto.StockLevelRequestDTO;
import com.streaem.productmanager.model.Product;
import com.streaem.productmanager.restclient.dto.ProductDTO;
import com.streaem.productmanager.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

@WebMvcTest(ProductController.class)
@Import(AppTestConfiguration.class)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void given_validDataButInStockAsNull_when_getAllProductByCategory_should_returnAllProduct() throws Exception {
        Mockito.when(productService.getAllProductByCategory(Mockito.anyString(), Mockito.isNull())).thenReturn(List.of(
                getSampleProductResponseDTO("sampleCategory", "sampleProduct", "sampleDescription", BigDecimal.ONE, 1),
                getSampleProductResponseDTO("sampleCategory", "sampleProduct2", "sampleDescription", BigDecimal.TEN, 0)
        ));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/api/v1/categories/sampleCategory/products"));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        ResponseDTO responseDTO = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.isSuccess());
        Assertions.assertEquals("processed successfully", responseDTO.getMessage());
        Assertions.assertEquals(2, ((List) responseDTO.getData()).size());
    }

    @Test
    void given_validDataButInStockAsTrue_when_getAllProductByCategory_should_returnInStockProducts() throws Exception {
        Mockito.when(productService.getAllProductByCategory(Mockito.anyString(), Mockito.eq(Boolean.TRUE))).thenReturn(List.of(
                getSampleProductResponseDTO("sampleCategory", "sampleProduct", "sampleDescription", BigDecimal.ONE, 1)
        ));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/api/v1/categories/sampleCategory/products?instock=true"));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        ResponseDTO responseDTO = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.isSuccess());
        Assertions.assertEquals("processed successfully", responseDTO.getMessage());
        Assertions.assertEquals(1, ((List) responseDTO.getData()).size());
    }

    @Test
    void given_validDataButInStockAsFalse_when_getAllProductByCategory_should_returnOutOfStockProducts() throws Exception {
        Mockito.when(productService.getAllProductByCategory(Mockito.anyString(), Mockito.eq(Boolean.FALSE))).thenReturn(List.of(
                getSampleProductResponseDTO("sampleCategory", "sampleProduct2", "sampleDescription", BigDecimal.ONE, 2)
        ));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/api/v1/categories/sampleCategory/products?instock=false"));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        ResponseDTO responseDTO = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.isSuccess());
        Assertions.assertEquals("processed successfully", responseDTO.getMessage());
        Assertions.assertEquals(1, ((List) responseDTO.getData()).size());
    }

    @Test
    void given_validData_when_getProduct_should_returnProductInfo() throws Exception {
        Mockito.when(productService.getProduct(Mockito.anyString(), Mockito.anyString())).thenReturn(getSampleProductResponseDTO("sampleCategory",
                "sampleProduct2", "sampleDescription", BigDecimal.ONE, 2));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/api/v1/categories/sampleCategory/products/sampleProduct2"));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        ResponseDTO responseDTO = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.isSuccess());
        Assertions.assertEquals("processed successfully", responseDTO.getMessage());
        LinkedHashMap<String, Object> data = (LinkedHashMap) responseDTO.getData();
        Assertions.assertNotNull(data);
        Assertions.assertEquals("sampleProduct2", data.get("name"));
        Assertions.assertEquals("sampleCategory", data.get("category"));
        Assertions.assertEquals("sampleDescription", data.get("description"));
        Assertions.assertEquals(1, data.get("price"));
        Assertions.assertEquals(2, data.get("stockLevel"));
    }

    @Test
    void given_validData_when_updateProduct_should_returnUpdatedProductInfo() throws Exception {
        Mockito.when(productService.updateProduct(Mockito.anyString(), Mockito.anyString(), Mockito.any(ProductRequestDTO.class)))
                .thenReturn(getSampleProductResponseDTO("sampleCategory",
                "sampleProduct2", "sampleDescription", BigDecimal.ONE, 2));

        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName("sampleProduct2");
        productRequestDTO.setCategory("sampleCategory");
        productRequestDTO.setDescription("sampleDescription");
        productRequestDTO.setPrice(BigDecimal.ONE);
        productRequestDTO.setStockLevel(2);
        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.put("/api/v1/categories/sampleCategory/products/sampleProduct2")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(productRequestDTO)));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        ResponseDTO responseDTO = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.isSuccess());
        Assertions.assertEquals("processed successfully", responseDTO.getMessage());
        LinkedHashMap<String, Object> data = (LinkedHashMap) responseDTO.getData();
        Assertions.assertNotNull(data);
        Assertions.assertEquals("sampleProduct2", data.get("name"));
        Assertions.assertEquals("sampleCategory", data.get("category"));
        Assertions.assertEquals("sampleDescription", data.get("description"));
        Assertions.assertEquals(1, data.get("price"));
        Assertions.assertEquals(2, data.get("stockLevel"));
    }

    @Test
    void given_invalidData_when_updateProduct_should_returnHttpStatusBadRequest() throws Exception {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName(null);
        productRequestDTO.setCategory("##4%^");
        productRequestDTO.setDescription("");
        productRequestDTO.setPrice(new BigDecimal("1.0234"));
        productRequestDTO.setStockLevel(2);
        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.put("/api/v1/categories/sampleCategory/products/sampleProduct2")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(productRequestDTO)));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mockHttpServletResponse.getStatus());

        ResponseDTO responseDTO = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.isSuccess());
        Assertions.assertTrue(responseDTO.getMessage().contains("numeric value out of bounds (<1000000000 digits>.<2 digits> expected)"));
        Assertions.assertTrue(responseDTO.getMessage().contains("invalid category provided, should not contain special character."));
        Assertions.assertTrue(responseDTO.getMessage().contains("name is required"));
        Assertions.assertTrue(responseDTO.getMessage().contains("description is required"));
    }

    @Test
    void given_validData_when_updateProductStockLevel_should_returnUpdatedProductInfo() throws Exception {
        Mockito.when(productService.updateProductStockLevel(Mockito.anyString(), Mockito.anyString(), Mockito.any(StockLevelRequestDTO.class)))
                .thenReturn(getSampleProductResponseDTO("sampleCategory",
                        "sampleProduct2", "sampleDescription", BigDecimal.ONE, 2));

        StockLevelRequestDTO stockLevelRequestDTO = new StockLevelRequestDTO();
        stockLevelRequestDTO.setStockLevel(2);
        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.patch("/api/v1/categories/sampleCategory/products/sampleProduct2")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(stockLevelRequestDTO)));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        ResponseDTO responseDTO = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.isSuccess());
        Assertions.assertEquals("processed successfully", responseDTO.getMessage());
        LinkedHashMap<String, Object> data = (LinkedHashMap) responseDTO.getData();
        Assertions.assertNotNull(data);
        Assertions.assertEquals("sampleProduct2", data.get("name"));
        Assertions.assertEquals("sampleCategory", data.get("category"));
        Assertions.assertEquals("sampleDescription", data.get("description"));
        Assertions.assertEquals(1, data.get("price"));
        Assertions.assertEquals(2, data.get("stockLevel"));
    }

    @Test
    void given_invalidData_when_updateProductStockLevel_should_returnHttpStatusBadRequest() throws Exception {
        StockLevelRequestDTO stockLevelRequestDTO = new StockLevelRequestDTO();
        stockLevelRequestDTO.setStockLevel(-1);
        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.patch("/api/v1/categories/sampleCategory/products/sampleProduct2")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(stockLevelRequestDTO)));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mockHttpServletResponse.getStatus());

        ResponseDTO responseDTO = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.isSuccess());
        Assertions.assertEquals("[stockLevel must be greater than or equal to zero]", responseDTO.getMessage());

        stockLevelRequestDTO.setStockLevel(null);
        MockHttpServletResponse mockHttpServletResponse1 = getMockMvc(mockMvc, MockMvcRequestBuilders.patch("/api/v1/categories/sampleCategory/products/sampleProduct2")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(stockLevelRequestDTO)));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mockHttpServletResponse.getStatus());

        responseDTO = new ObjectMapper().readValue(mockHttpServletResponse1.getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.isSuccess());
        Assertions.assertEquals("[stockLevel is required]", responseDTO.getMessage());
    }

    private MockHttpServletResponse getMockMvc(MockMvc mockMvc, MockHttpServletRequestBuilder mockHttpServletRequestBuilder)
            throws Exception {
        return mockMvc.perform(mockHttpServletRequestBuilder
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();
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