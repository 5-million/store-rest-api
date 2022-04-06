package xyz.fm.storerestapi.controller.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.LoginException;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;
import xyz.fm.storerestapi.util.EncryptUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(properties = {
        "jwt.expired=604800"
})
public class ConsumerRestControllerLoginTest extends ConsumerRestControllerTest {

    private static final String URL = BASE_URL + "/login";

    @Test
    @DisplayName("login fail: invalid email")
    public void login_fail_invalidEmail() throws Exception {
        //given
        LoginRequest request = new LoginRequest("abc", "password");

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_EMAIL_FORMAT));
    }

    @Test
    @DisplayName("login fail: not found user")
    public void login_fail_notFoundUser() throws Exception {
        //given
        LoginRequest request = new LoginRequest("abc@test.com", "password");

        given(consumerService.login(any(LoginRequest.class)))
                .willThrow(new LoginException(Error.LOGIN_FAIL, ErrorDetail.NOT_FOUND_USER, true));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.LOGIN_FAIL.getCode()));

        LoginException exception = (LoginException) ra.andReturn().getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_USER);
    }

    @Test
    @DisplayName("login fail: incorrect password")
    public void login_fail_incorrectPassword() throws Exception {
        //given
        LoginRequest request = new LoginRequest("abc@test.com", "password");

        given(consumerService.login(any(LoginRequest.class)))
                .willThrow(new LoginException(Error.LOGIN_FAIL, ErrorDetail.INCORRECT_PWD, true));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.LOGIN_FAIL.getCode()));

        LoginException exception = (LoginException) ra.andReturn().getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.INCORRECT_PWD);
    }

    @Test
    @DisplayName("login success")
    public void login_success() throws Exception {
        //given
        LoginRequest request = new LoginRequest("abc@test.com", "password");
        Consumer consumer = new Consumer.Builder(
                request.getEmail(),
                "name",
                EncryptUtil.encode(request.getPassword()),
                "01012345678",
                new AdsReceive(true, true, true)
        )
                .id(1L)
                .build();

        given(consumerService.login(any(LoginRequest.class))).willReturn(consumer);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consumerId").value(consumer.getId()))
                .andExpect(jsonPath("$.email").value(consumer.getEmail()))
                .andExpect(jsonPath("$.name").value(consumer.getName()))
                .andExpect(jsonPath("$.addedCartCount").value(0))
                .andExpect(cookie().exists(JwtTokenUtil.JWT_KEY));
    }
}
