package xyz.fm.storerestapi.controller.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.user.WithdrawalRequest;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ConsumerRestControllerWithdrawalTest extends ConsumerRestControllerTest {

    private final String url = BASE_URL + "/withdrawal";
    private final String requesterEmail = "abc@test.com";

    @Test
    @DisplayName("withdrawal fail: 토큰이 포함되지 않음")
    public void withdrawal_fail_tokenNotIncluded() throws Exception {
        //given
        WithdrawalRequest request = new WithdrawalRequest("consumer", "password");

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.delete(url)
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
    @DisplayName("withdrawal fail: incorrect name")
    public void withdrawal_fail_incorrectName() throws Exception {
        //given
        WithdrawalRequest request = new WithdrawalRequest("consumer", "password");
        given(consumerService.withdrawal(anyString(), any(WithdrawalRequest.class)))
                .willThrow(new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.INCORRECT_NAME));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .header(JwtTokenUtil.JWT_KEY, JwtTokenUtil.JWT_VALUE_PREFIX + requesterEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("withdrawal"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.INCORRECT_NAME));
        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("withdrawal fail: incorrect password")
    public void withdrawal_fail_incorrectPassword() throws Exception {
        //given
        WithdrawalRequest request = new WithdrawalRequest("consumer", "password");
        given(consumerService.withdrawal(anyString(), any(WithdrawalRequest.class)))
                .willThrow(new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.INCORRECT_PWD));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .header(JwtTokenUtil.JWT_KEY, JwtTokenUtil.JWT_VALUE_PREFIX + requesterEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("withdrawal"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.INCORRECT_PWD));
        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("withdrawal success")
    public void withdrawal_success() throws Exception {
        //given
        WithdrawalRequest request = new WithdrawalRequest("consumer", "password");
        given(consumerService.withdrawal(anyString(), any(WithdrawalRequest.class))).willReturn(true);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .header(JwtTokenUtil.JWT_KEY, JwtTokenUtil.JWT_VALUE_PREFIX + requesterEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("withdrawal"))
                .andExpect(status().isNoContent());
    }
}
