package com.streaem.productmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final ProductService productService;

    public Set<String> findAllCategories() {
        return productService.getAllProductCategories();
    }

}
