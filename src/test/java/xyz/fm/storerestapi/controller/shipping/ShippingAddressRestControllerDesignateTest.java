package xyz.fm.storerestapi.controller.shipping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_KEY;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_VALUE_PREFIX;

public class ShippingAddressRestControllerDesignateTest extends ShippingAddressRestControllerTest {

    private long shippingAddressId = 2;
    private String url = BASE_URL + "/default/" + shippingAddressId;
    private Consumer consumer;
    private String token;

    @BeforeEach
    void beforeEach() {
        consumer = new Consumer.Builder(
                "consumer@test.com",
                "consumer",
                "password",
                "01012345678",
                new AdsReceive(true, false, true)
        ).id(1L).build();

        token = jwtTokenUtil.generateToken(consumer);
    }

    @Test
    @DisplayName("designateDefault fail: token not included")
    public void designateDefault_fail_tokenNotIncluded() throws Exception {
        //given


        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(url)
        ).andDo(print());

        //then
        ra
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("designateDefault fail: not found consumer")
    public void designateDefault_fail_notFoundConsumer() throws Exception {
        //given
        given(consumerService.getByEmail(anyString())).willThrow(
                new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_USER)
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("designateDefault"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_USER));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("designateDefault fail: not found shipping address")
    public void designateDefault_fail_notFoundShippingAddress() throws Exception {
        //given
        given(consumerService.getByEmail(anyString())).willReturn(consumer);
        doThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS))
                .when(shippingAddressService)
                .designateDefaultShippingAddress(any(Consumer.class), anyLong());

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("designateDefault"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("designateDefault success")
    public void designateDefault_success() throws Exception {
        //given
        given(consumerService.getByEmail(anyString())).willReturn(consumer);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("designateDefault"))
                .andExpect(status().isNoContent());

        verify(shippingAddressService, times(1)).designateDefaultShippingAddress(any(Consumer.class), anyLong());
    }
}
