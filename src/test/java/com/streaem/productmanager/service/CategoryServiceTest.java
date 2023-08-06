package com.streaem.productmanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    void given_validDate_when_findAllCategories_should_return_listOfCategories() {
        Mockito.when(productService.getAllProductCategories()).thenReturn(Set.of("SampleCategoryName", "SampleCategoryName2"));
        Set<String> allCategories = categoryService.findAllCategories();
        Assertions.assertNotNull(allCategories);
        Assertions.assertEquals(2, allCategories.size());
    }
}