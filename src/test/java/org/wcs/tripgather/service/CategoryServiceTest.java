package org.wcs.tripgather.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.wcs.tripgather.dto.CategoryDTO;
import org.wcs.tripgather.mapper.CategoryMapper;
import org.wcs.tripgather.model.Category;
import org.wcs.tripgather.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        List<Category> categories = List.of(new Category());
        List<CategoryDTO> categoryDTOs = List.of(new CategoryDTO());

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.convertToDTO(any(Category.class))).thenReturn(categoryDTOs.get(0));

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertEquals(categoryDTOs.size(), result.size());
    }

    @Test
    void testGetCategoryById_WhenExists() {
        Category category = new Category();
        CategoryDTO categoryDTO = new CategoryDTO();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.convertToDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertNotNull(result);
    }

    @Test
    void testGetCategoryById_WhenNotExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertNull(result);
    }

    @Test
    void testAddCategory() {
        CategoryDTO categoryDTO = new CategoryDTO();
        Category category = new Category();

        when(categoryMapper.convertToEntity(categoryDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.convertToDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.addCategory(categoryDTO);

        assertNotNull(result);
    }

    @Test
    void testUpdateCategory_WhenExists() {
        Category category = new Category();
        CategoryDTO categoryDTO = new CategoryDTO();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.convertToDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.updateCategory(1L, categoryDTO);

        assertNotNull(result);
    }

    @Test
    void testUpdateCategory_WhenNotExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(1L, new CategoryDTO()));
    }

    @Test
    void testDeleteCategory_WhenExists() {
        Category category = new Category();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        boolean result = categoryService.deleteCategory(1L);

        assertTrue(result);
    }

    @Test
    void testDeleteCategory_WhenNotExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = categoryService.deleteCategory(1L);

        assertFalse(result);
    }
}