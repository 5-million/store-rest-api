package xyz.fm.storerestapi.service.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.user.PasswordChangeRequest;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.InvalidPasswordException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;
import xyz.fm.storerestapi.util.EncryptUtil;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class ConsumerServiceChangeTest extends ConsumerServiceTest {

    private final String requesterEmail = "abc@test.com";
    private Consumer buildTestConsumer() {
        return new Consumer.Builder(
                requesterEmail,
                "consumer",
                EncryptUtil.encode("password"),
                "01012345678",
                new AdsReceive(true, true, true)
        ).id(1L).build();
    }

    @Test
    @DisplayName("changePassword fail: newPassword != confirmNewPassword")
    public void changePassword_fail_newPwdNotEqualToConfirmNewPwd() {
        //given
        PasswordChangeRequest request = new PasswordChangeRequest("password", "newpassword", "confirmNewPassword");

        //when
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> consumerService.changePassword(requesterEmail, request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.INVALID_PASSWORD);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD);
    }

    @Test
    @DisplayName("changePassword fail: not found user")
    public void changePassword_fail_notFoundUser() {
        //given
        PasswordChangeRequest request = new PasswordChangeRequest("password", "newpassword", "newpassword");
        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> consumerService.changePassword(requesterEmail, request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_USER);
    }

    @Test
    @DisplayName("changePassword fail: incorrect password")
    public void changePassword_fail_incorrectPassword() {
        //given
        PasswordChangeRequest request = new PasswordChangeRequest("qwer", "newpassword", "newpassword");
        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.of(buildTestConsumer()));

        //when
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> consumerService.changePassword(requesterEmail, request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.UNAUTHORIZED);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.INCORRECT_PWD);
    }

    @Test
    @DisplayName("changePassword success")
    public void changePassword_success() {
        //given
        PasswordChangeRequest request = new PasswordChangeRequest("password", "newpassword", "newpassword");
        Consumer consumer = buildTestConsumer();
        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.of(consumer));

        //when
        consumerService.changePassword(requesterEmail, request);

        //then
        assertThat(consumer.isMatchedPassword(request.getNewPassword())).isTrue();
    }
}
