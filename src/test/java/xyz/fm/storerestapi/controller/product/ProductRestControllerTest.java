package xyz.fm.storerestapi.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;
import xyz.fm.storerestapi.service.product.ProductService;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    protected static final String BASE_URL = "/api/product";
    protected static final ObjectMapper om = new ObjectMapper();

    @Autowired protected MockMvc mvc;
    @MockBean protected ProductService productService;
    @SpyBean protected JwtTokenUtil jwtTokenUtil;
}