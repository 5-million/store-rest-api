package xyz.fm.storerestapi.controller.shipping;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressInfo;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_KEY;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_VALUE_PREFIX;

public class ShippingAddressRestControllerGetTest extends ShippingAddressRestControllerTest {

    private long shippingAddressId = 5;
    private String getByIdUrl = BASE_URL + "/" + shippingAddressId;
    private String getByConsumerUrl = BASE_URL;
    private String token = "consumer@test.com";

    @Test
    @DisplayName("getById fail: token not included")
    public void getById_fail_tokenNotIncluded() throws Exception {
        //given


        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(getByIdUrl)
        ).andDo(print());

        //then
        ra
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("getById fail: not found shipping address")
    public void getById_fail_notFoundShippingAddress() throws Exception {
        //given
        given(shippingAddressApiRepository.findByIdAndConsumerEmail(anyLong(), anyString()))
                .willReturn(Optional.empty());

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(getByIdUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("getById"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("getById success")
    public void getById_success() throws Exception {
        //given
        ShippingAddressInfo shippingAddressInfo = new ShippingAddressInfo(
                1L, 2L, "zipcode", "address", "detailedAddress", true
        );

        given(shippingAddressApiRepository.findByIdAndConsumerEmail(anyLong(), anyString()))
                .willReturn(Optional.of(shippingAddressInfo));


        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(getByIdUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("getById"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consumerId").value(shippingAddressInfo.getConsumerId()))
                .andExpect(jsonPath("$.shippingAddressId").value(shippingAddressInfo.getShippingAddressId()))
                .andExpect(jsonPath("$.zipcode").value(shippingAddressInfo.getZipcode()))
                .andExpect(jsonPath("$.address").value(shippingAddressInfo.getAddress()))
                .andExpect(jsonPath("$.detailedAddress").value(shippingAddressInfo.getDetailedAddress()))
                .andExpect(jsonPath("$.isDefaultAddress").value(shippingAddressInfo.getIsDefaultAddress()));
    }

    @Test
    @DisplayName("getByConsumer fail: token not included")
    public void getByConsumer_fail_tokenNotIncluded() throws Exception {
        //given


        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(getByConsumerUrl)
        ).andDo(print());

        //then
        ra
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("getByConsumer success: empty")
    public void getByConsumer_success_empty() throws Exception {
        //given
        given(shippingAddressApiRepository.findByConsumerEmail(anyString()))
                .willReturn(Collections.EMPTY_LIST);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(getByConsumerUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("getByConsumer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("shippingAddresses").isArray())
                .andExpect(jsonPath("shippingAddresses").isEmpty());
    }

    @Test
    @DisplayName("getByConsumer success: not empty")
    public void getByConsumer_success_notEmpty() throws Exception {
        //given
        List<ShippingAddressInfo> shippingAddressInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            shippingAddressInfos.add(
                    new ShippingAddressInfo((long) i, (long) i, "zipcode" + i, "address" + i, "detailedAddress" + i, false)
            );
        }

        given(shippingAddressApiRepository.findByConsumerEmail(anyString())).willReturn(shippingAddressInfos);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(getByConsumerUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ShippingAddressRestControllerImpl.class))
                .andExpect(handler().methodName("getByConsumer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("shippingAddresses").isArray())
                .andExpect(jsonPath("shippingAddresses").isNotEmpty());

        for (int i = 0; i < 10; i++) {
            ra
                    .andExpect(jsonPath("$.shippingAddresses[" + i + "].consumerId").value(shippingAddressInfos.get(i).getConsumerId()))
                    .andExpect(jsonPath("$.shippingAddresses[" + i + "].shippingAddressId").value(shippingAddressInfos.get(i).getShippingAddressId()))
                    .andExpect(jsonPath("$.shippingAddresses[" + i + "].zipcode").value(shippingAddressInfos.get(i).getZipcode()))
                    .andExpect(jsonPath("$.shippingAddresses[" + i + "].address").value(shippingAddressInfos.get(i).getAddress()))
                    .andExpect(jsonPath("$.shippingAddresses[" + i + "].detailedAddress").value(shippingAddressInfos.get(i).getDetailedAddress()))
                    .andExpect(jsonPath("$.shippingAddresses[" + i + "].isDefaultAddress").value(shippingAddressInfos.get(i).getIsDefaultAddress()));
        }
    }
}
