package com.streaem.productmanager.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
@ConfigurationProperties(prefix = "app.config")
public class AppConfig {

    @NotBlank(message = "app.config.productBaseUrl is required")
    private String productBaseUrl;

    public String getProductBaseUrl() {
        return productBaseUrl;
    }

    public void setProductBaseUrl(String productBaseUrl) {
        this.productBaseUrl = productBaseUrl;
    }
}
