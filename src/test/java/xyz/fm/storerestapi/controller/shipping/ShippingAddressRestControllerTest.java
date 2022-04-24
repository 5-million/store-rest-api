package xyz.fm.storerestapi.controller.shipping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;
import xyz.fm.storerestapi.repository.shipping.ShippingAddressApiRepository;
import xyz.fm.storerestapi.service.shipping.ShippingAddressService;
import xyz.fm.storerestapi.service.user.consumer.ConsumerService;

@WebMvcTest(ShippingAddressRestControllerImpl.class)
class ShippingAddressRestControllerTest {

    protected static final String BASE_URL = "/api/shipping/address";
    protected static final ObjectMapper om = new ObjectMapper();

    @Autowired protected MockMvc mvc;
    @MockBean protected ShippingAddressService shippingAddressService;
    @MockBean protected ShippingAddressApiRepository shippingAddressApiRepository;
    @MockBean protected ConsumerService consumerService;
    @SpyBean protected JwtTokenUtil jwtTokenUtil;
}