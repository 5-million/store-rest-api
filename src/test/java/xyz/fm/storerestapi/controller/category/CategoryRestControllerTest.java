package xyz.fm.storerestapi.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import xyz.fm.storerestapi.controller.CategoryRestController;
import xyz.fm.storerestapi.controller.ControllerTestConfig;
import xyz.fm.storerestapi.service.CategoryService;

@WebMvcTest(CategoryRestController.class)
class CategoryRestControllerTest extends ControllerTestConfig {

    protected static final String BASE_URL = "/categories";
    protected static final ObjectMapper om = new ObjectMapper();

    @MockBean protected CategoryService categoryService;
    @Autowired protected MockMvc mvc;
}