package xyz.fm.storerestapi.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import xyz.fm.storerestapi.service.category.CategoryService;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CategoryRestController.class)
abstract class CategoryRestControllerTest {

    protected static final String BASE_URL = "/api/category";
    protected static final ObjectMapper om = new ObjectMapper();

    @Autowired protected MockMvc mvc;
    @MockBean protected CategoryService categoryService;
}