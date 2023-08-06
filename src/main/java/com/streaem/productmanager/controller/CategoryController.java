package com.streaem.productmanager.controller;

import com.streaem.productmanager.dto.ResponseDTO;
import com.streaem.productmanager.dto.SuccessResponseDTO;
import com.streaem.productmanager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllCategories() {
        Set<String> allCategories = categoryService.findAllCategories();
        return ResponseEntity.ok(new SuccessResponseDTO(allCategories));
    }

}
