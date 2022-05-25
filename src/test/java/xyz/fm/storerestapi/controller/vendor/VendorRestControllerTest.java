package xyz.fm.storerestapi.controller.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import xyz.fm.storerestapi.controller.ControllerTestConfig;
import xyz.fm.storerestapi.controller.VendorRestController;
import xyz.fm.storerestapi.service.VendorService;

@WebMvcTest(VendorRestController.class)
public abstract class VendorRestControllerTest extends ControllerTestConfig {

    protected static final ObjectMapper om = new ObjectMapper();

    @Autowired protected MockMvc mvc;
    @MockBean protected VendorService vendorService;
}