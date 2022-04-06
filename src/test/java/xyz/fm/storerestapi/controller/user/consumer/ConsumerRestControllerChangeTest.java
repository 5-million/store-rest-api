package xyz.fm.storerestapi.controller.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.user.PasswordChangeRequest;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.InvalidPasswordException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_KEY;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_VALUE_PREFIX;

public class ConsumerRestControllerChangeTest extends ConsumerRestControllerTest {

    private final String url = BASE_URL + "/change/pwd";
    private final String requesterEmail = "abc@test.com";

    @Test
    @DisplayName("changePassword fail: 토큰이 포함되지 않음")
    public void changePassword_fail_tokenNotIncluded() throws Exception {
        //given
        PasswordChangeRequest request = new PasswordChangeRequest("password", "new password", "new password");

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(url)
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
    @DisplayName("changePassword fail: not found user")
    public void changePassword_fail_notFoundUser() throws Exception {
        //given
        PasswordChangeRequest request = new PasswordChangeRequest("password", "new password", "new password");
        doThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_USER))
                .when(consumerService)
                .changePassword(anyString(), any(PasswordChangeRequest.class));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + requesterEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_USER));
        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("changePassword fail: incorrect password")
    public void changePassword_fail_incorrectPassword() throws Exception {
        //given
        PasswordChangeRequest request = new PasswordChangeRequest("password", "new password", "new password");
        doThrow(new UnauthorizedException(Error.INVALID_PASSWORD, ErrorDetail.INCORRECT_PWD))
                .when(consumerService)
                .changePassword(anyString(), any(PasswordChangeRequest.class));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + requesterEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("changePassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.INVALID_PASSWORD.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.INCORRECT_PWD));
        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("changePassword fail: newPwd != confirmNewPwd")
    public void changePassword_fail_newPwdNotEqualToConfirmNewPwd() throws Exception {
        //given
        PasswordChangeRequest request = new PasswordChangeRequest("password", "new password", "confirm new password");
        doThrow(new InvalidPasswordException(Error.INVALID_PASSWORD, ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD))
                .when(consumerService)
                .changePassword(anyString(), any(PasswordChangeRequest.class));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + requesterEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("changePassword"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.INVALID_PASSWORD.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD));
        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(InvalidPasswordException.class);
    }

    @Test
    @DisplayName("changePassword success")
    public void changePassword_success() throws Exception {
        //given
        PasswordChangeRequest request = new PasswordChangeRequest("password", "new password", "new password");

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + requesterEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("changePassword"))
                .andExpect(status().isOk());
    }
}
