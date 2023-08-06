package com.streaem.productmanager.restclient;

import com.streaem.productmanager.config.FeignClientConfiguration;
import com.streaem.productmanager.restclient.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "product-client", url = "${app.config.productBaseUrl}", configuration = FeignClientConfiguration.class)
public interface ProductAPIClient {

    @GetMapping("/productdata")
    List<ProductDTO> getProductData();

}
