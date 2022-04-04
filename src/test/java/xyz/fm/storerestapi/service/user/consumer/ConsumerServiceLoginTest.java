package xyz.fm.storerestapi.service.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.LoginException;
import xyz.fm.storerestapi.util.EncryptUtil;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class ConsumerServiceLoginTest extends ConsumerServiceTest {

    @Test
    @DisplayName("login fail: not found user")
    public void login_fail_notFoundUser() {
        //given
        LoginRequest request = new LoginRequest("abc@test.com", "password");
        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        LoginException exception = assertThrows(LoginException.class, () -> consumerService.login(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.LOGIN_FAIL);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_USER);
        assertThat(exception.isDetailIgnoreForUser()).isTrue();
    }

    @Test
    @DisplayName("login fail: incorrect password")
    public void login_fail_incorrectPassword() {
        //given
        LoginRequest request = new LoginRequest("abc@test.com", "pessword");
        Consumer consumer = new Consumer.Builder(
                "abc@test.com",
                "name",
                EncryptUtil.encode("password"),
                "01012345678",
                new AdsReceive(true, true, true)
        ).build();

        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.of(consumer));

        //when
        LoginException exception = assertThrows(LoginException.class, () -> consumerService.login(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.LOGIN_FAIL);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.INCORRECT_PWD);
        assertThat(exception.isDetailIgnoreForUser()).isTrue();
    }

    @Test
    @DisplayName("login success")
    public void login_success() {
        //given
        LoginRequest request = new LoginRequest("abc@test.com", "password");
        Consumer consumer = new Consumer.Builder(
                "abc@test.com",
                "name",
                EncryptUtil.encode(request.getPassword()),
                "01012345678",
                new AdsReceive(true, true, true)
        ).build();

        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.of(consumer));

        //when
        Consumer result = consumerService.login(request);

        //then
        assertThat(result.getLastLoginDate()).isNotNull();
    }
}
