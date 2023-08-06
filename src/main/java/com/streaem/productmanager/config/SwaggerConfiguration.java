package com.streaem.productmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .version("1.0")
                        .title("Streaem Product Manager")
                        .description("Help to fetch and manage streaem product")
                        .contact(new Contact()
                                .name("Streaem")
                                .url("https://streaem.com")
                                .email("info@streaem.com")));
    }

}
