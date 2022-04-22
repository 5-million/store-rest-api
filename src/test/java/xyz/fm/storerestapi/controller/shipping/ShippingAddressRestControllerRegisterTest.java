package xyz.fm.storerestapi.controller.shipping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressRegisterRequest;
import xyz.fm.storerestapi.entity.shipping.Address;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_KEY;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_VALUE_PREFIX;

public class ShippingAddressRestControllerRegisterTest extends ShippingAddressRestControllerTest {

    private Consumer consumer;
    private String token;

    @BeforeEach
    void beforeEach() {
        consumer = new Consumer.Builder(
                "consumer@test.com",
                "consumer",
                "pwd",
                "01012345678",
                new AdsReceive(false, true, false)
        ).id(1L).build();

        token = jwtTokenUtil.generateToken(consumer);
    }

    @Test
    @DisplayName("register fail: token not included")
    public void register_fail_tokenNotIncluded() throws Exception {
        //given
        ShippingAddressRegisterRequest request = new ShippingAddressRegisterRequest(
                "05510", "서울특별시 송파구 송파대로 570", "8~26층"
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
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
    @DisplayName("register fail: zipcode is blank")
    public void register_fail_zipcodeIsBlank() throws Exception {
        //given
        ShippingAddressRegisterRequest request = new ShippingAddressRegisterRequest(
                "", "서울특별시 송파구 송파대로 570", "8~26층"
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
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
        ShippingAddressRegisterRequest request = new ShippingAddressRegisterRequest(
                "05510", "", "8~26층"
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
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
        ShippingAddressRegisterRequest request = new ShippingAddressRegisterRequest(
                "05510", "서울특별시 송파구 송파대로 570", ""
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
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
    @DisplayName("register fail: not found consumer")
    public void register_fail_notFoundConsumer() throws Exception {
        //given
        ShippingAddressRegisterRequest request = new ShippingAddressRegisterRequest(
                "05510", "서울특별시 송파구 송파대로 570", "8~26층"
        );

        given(consumerService.getByEmail(anyString()))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_USER));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("register"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_USER));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("register success")
    public void register_success() throws Exception {
        //given
        ShippingAddressRegisterRequest request = new ShippingAddressRegisterRequest(
                "05510", "서울특별시 송파구 송파대로 570", "8~26층"
        );

        Address address = new Address(request.getZipcode(), request.getAddress(), request.getDetailedAddress());
        ShippingAddress shippingAddress = new ShippingAddress.Builder(address).id(2L).build();
        consumer.addShippingAddress(shippingAddress);

        given(consumerService.getByEmail(anyString())).willReturn(consumer);
        given(shippingAddressService.register(any(Consumer.class), any(ShippingAddressRegisterRequest.class)))
                .willReturn(shippingAddress);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("register"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.consumerId").value(shippingAddress.getConsumer().getId()))
                .andExpect(jsonPath("$.shippingAddressId").value(shippingAddress.getId()))
                .andExpect(jsonPath("$.zipcode").value(shippingAddress.getAddress().getZipcode()))
                .andExpect(jsonPath("$.address").value(shippingAddress.getAddress().getAddress()))
                .andExpect(jsonPath("$.detailedAddress").value(shippingAddress.getAddress().getDetailedAddress()))
                .andExpect(jsonPath("$.isDefaultAddress").value(shippingAddress.isDefaultAddress()));
    }
}
