package xyz.fm.storerestapi.controller.shipping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressModifyRequest;
import xyz.fm.storerestapi.entity.shipping.Address;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotEntityOwnerException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_KEY;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_VALUE_PREFIX;

public class ShippingAddressRestControllerModifyTest extends ShippingAddressRestControllerTest {

    private String token;
    private ShippingAddress originShippingAddress;
    private long shippingAddressId = 2;
    private String url = BASE_URL + "/" + shippingAddressId;

    @BeforeEach
    void beforeEach() {
        Consumer consumer = new Consumer.Builder(
                "consumer@test.com",
                "consumer",
                "pwd",
                "01012345678",
                new AdsReceive(false, true, false)
        ).id(1L).build();

        token = jwtTokenUtil.generateToken(consumer);

        originShippingAddress = new ShippingAddress.Builder(
                new Address("originZipcode", "originAddress", "originDetailedAddress")
        ).id(2L).consumer(consumer).build();
    }

    @Test
    @DisplayName("modify fail: token not included")
    public void modify_fail_tokenNotIncluded() throws Exception {
        //given
        ShippingAddressModifyRequest request = new ShippingAddressModifyRequest(
                "05510", "서울특별시 송파구 송파대로 570", "8~26층"
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("modify fail: zipcode is blank")
    public void modify_fail_zipcodeIsBlank() throws Exception {
        //given
        ShippingAddressModifyRequest request = new ShippingAddressModifyRequest(
                "", "서울특별시 송파구 송파대로 570", "8~26층"
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.put(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(MethodArgumentNotValidException.class);
    }

    @Test
    @DisplayName("register fail: address is blank")
    public void register_fail_addressIsBlank() throws Exception {
        //given
        ShippingAddressModifyRequest request = new ShippingAddressModifyRequest(
                "05510", "", "8~26층"
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.put(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(MethodArgumentNotValidException.class);
    }

    @Test
    @DisplayName("register fail: detailedAddress is blank")
    public void register_fail_detailedAddressIsBlank() throws Exception {
        //given
        ShippingAddressModifyRequest request = new ShippingAddressModifyRequest(
                "05510", "서울특별시 송파구 송파대로 570", ""
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.put(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(MethodArgumentNotValidException.class);
    }

    @Test
    @DisplayName("modify fail: not found shipping address")
    public void modify_fail_notFoundShippingAddress() throws Exception {
        //given
        ShippingAddressModifyRequest request = new ShippingAddressModifyRequest(
                "05510", "서울특별시 송파구 송파대로 570", "8~26층"
        );

        doThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS))
                .when(shippingAddressService)
                .modify(anyString(), any(ShippingAddressModifyRequest.class));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.put(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("modify fail: not entity owner")
    public void modify_fail_notEntityOwner() throws Exception {
        //given
        ShippingAddressModifyRequest request = new ShippingAddressModifyRequest(
                "05510", "서울특별시 송파구 송파대로 570", "8~26층"
        );

        doThrow(new NotEntityOwnerException(Error.NO_PERMISSION, ErrorDetail.NOT_ENTITY_OWNER))
                .when(shippingAddressService)
                .modify(anyString(), any(ShippingAddressModifyRequest.class));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.put(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value(Error.NO_PERMISSION.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_ENTITY_OWNER));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotEntityOwnerException.class);
    }

    @Test
    @DisplayName("modify success")
    public void modify_success() throws Exception {
        //given
        ShippingAddressModifyRequest request = new ShippingAddressModifyRequest(
                "05510", "서울특별시 송파구 송파대로 570", "8~26층"
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.put(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().isNoContent());

        verify(shippingAddressService, times(1)).modify(anyString(), any(ShippingAddressModifyRequest.class));
    }
}
