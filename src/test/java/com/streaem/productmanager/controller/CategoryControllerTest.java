package com.streaem.productmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaem.productmanager.dto.ResponseDTO;
import com.streaem.productmanager.service.CategoryService;
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

import java.util.List;
import java.util.Set;

@WebMvcTest(CategoryController.class)
@Import(AppTestConfiguration.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void given_validData_when_getAllCategories_should_returnAllCategories() throws Exception {

        Mockito.when(categoryService.findAllCategories()).thenReturn(Set.of("SampleCategory", "SampleCategory2"));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/api/v1/categories"));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        ResponseDTO responseDTO = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), ResponseDTO.class);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertTrue(responseDTO.isSuccess());
        Assertions.assertEquals("processed successfully", responseDTO.getMessage());
        List<String> data = (List<String>) responseDTO.getData();
        Assertions.assertEquals(2, data.size());

    }

    private MockHttpServletResponse getMockMvc(MockMvc mockMvc, MockHttpServletRequestBuilder mockHttpServletRequestBuilder)
            throws Exception {
        return mockMvc.perform(mockHttpServletRequestBuilder
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();
    }
}