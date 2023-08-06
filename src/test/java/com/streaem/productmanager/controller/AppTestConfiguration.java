package com.streaem.productmanager.controller;

import com.streaem.productmanager.service.ProductService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AppTestConfiguration {

    @Bean
    public ProductService productService() {
        return Mockito.mock(ProductService.class);
    }
    
}
