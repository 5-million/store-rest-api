package xyz.fm.storerestapi.service.category;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.fm.storerestapi.repository.category.CategoryRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
abstract class CategoryServiceTest {

    @Mock protected CategoryRepository categoryRepository;
    @InjectMocks protected CategoryService categoryService;
}