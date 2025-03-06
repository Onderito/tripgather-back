package org.wcs.tripgather.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.wcs.tripgather.dto.CategoryDTO;
import org.wcs.tripgather.service.CategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories_WhenCategoriesExist() {
        List<CategoryDTO> categories = List.of(new CategoryDTO(), new CategoryDTO());
        when(categoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<CategoryDTO>> response = categoryController.getAllCategories();

        assertEquals(OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAllCategories_WhenNoCategoriesExist() {
        when(categoryService.getAllCategories()).thenReturn(List.of());

        ResponseEntity<List<CategoryDTO>> response = categoryController.getAllCategories();

        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetCategoryById_WhenCategoryExists() {
        CategoryDTO category = new CategoryDTO();
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        ResponseEntity<CategoryDTO> response = categoryController.getCategoryById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void testGetCategoryById_WhenCategoryDoesNotExist() {
        when(categoryService.getCategoryById(1L)).thenReturn(null);

        ResponseEntity<CategoryDTO> response = categoryController.getCategoryById(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testAddCategory() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("New Category");
        CategoryDTO savedCategory = new CategoryDTO();
        savedCategory.setId(1L);
        savedCategory.setName("New Category");
        when(categoryService.addCategory(categoryDTO)).thenReturn(savedCategory);

        ResponseEntity<CategoryDTO> response = categoryController.addCategory(categoryDTO);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(savedCategory, response.getBody());
    }

    @Test
    void testUpdateCategory_WhenCategoryExists() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Updated Category");
        CategoryDTO updatedCategory = new CategoryDTO();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Category");
        when(categoryService.updateCategory(1L, categoryDTO)).thenReturn(updatedCategory);

        ResponseEntity<CategoryDTO> response = categoryController.updateCategory(1L, categoryDTO);

        assertEquals(OK, response.getStatusCode());
        assertEquals(updatedCategory, response.getBody());
    }

    @Test
    void testUpdateCategory_WhenCategoryDoesNotExist() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Updated Category");
        when(categoryService.updateCategory(1L, categoryDTO)).thenReturn(null);

        ResponseEntity<CategoryDTO> response = categoryController.updateCategory(1L, categoryDTO);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteCategory_WhenCategoryExists() {
        when(categoryService.deleteCategory(1L)).thenReturn(true);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteCategory_WhenCategoryDoesNotExist() {
        when(categoryService.deleteCategory(1L)).thenReturn(false);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
    }
}
