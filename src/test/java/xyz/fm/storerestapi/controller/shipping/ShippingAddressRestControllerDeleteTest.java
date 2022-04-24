package xyz.fm.storerestapi.controller.shipping;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotEntityOwnerException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.*;

public class ShippingAddressRestControllerDeleteTest extends ShippingAddressRestControllerTest {

    private String token = "requester@test.com";
    private long shippingAddressId = 2;
    private String url = BASE_URL + "/" + shippingAddressId;

    @Test
    @DisplayName("delete fail: token not included")
    public void delete_fail_tokenNotIncluded() throws Exception {
        //given


        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.delete(url)
        ).andDo(print());

        //then
        ra
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("delete fail: not found shipping address")
    public void delete_fail_notFoundShippingAddress() throws Exception {
        //given
        doThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS))
                .when(shippingAddressService)
                .delete(anyString(), anyLong());

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("delete fail: not entity owner")
    public void delete_fail_notEntityOwner() throws Exception {
        //given
        doThrow(new NotEntityOwnerException(Error.NO_PERMISSION, ErrorDetail.NOT_ENTITY_OWNER))
                .when(shippingAddressService)
                .delete(anyString(), anyLong());

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value(Error.NO_PERMISSION.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_ENTITY_OWNER));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotEntityOwnerException.class);
    }

    @Test
    @DisplayName("delete success")
    public void delete_success() throws Exception {
        //given


        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        verify(shippingAddressService, times(1)).delete(anyString(), anyLong());
        ra.andExpect(status().isNoContent());
    }
}
