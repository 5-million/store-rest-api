package xyz.fm.storerestapi.service.user.consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.user.WithdrawalRequest;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;
import xyz.fm.storerestapi.util.EncryptUtil;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConsumerServiceWithdrawalTest extends ConsumerServiceTest {

    @Test
    @DisplayName("withdrawal fail: not found user")
    public void withdrawal_fail_notFoundUser() {
        //given
        String email = "abc@test.com";
        WithdrawalRequest request = new WithdrawalRequest("name", "password");

        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> consumerService.withdrawal(email, request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_USER);
    }

    @Test
    @DisplayName("withdrawal fail: name not match")
    public void withdrawal_fail_nameNotMatch() {
        //given
        String email = "abc@test.com";
        WithdrawalRequest request = new WithdrawalRequest("name", "password");

        Consumer consumer = new Consumer.Builder(
                email,
                "consumer",
                request.getPassword(),
                "01012345678",
                new AdsReceive(true, true, true)
        ).build();

        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.of(consumer));

        //when
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> consumerService.withdrawal(email, request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.UNAUTHORIZED);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.INCORRECT_NAME);
    }

    @Test
    @DisplayName("withdrawal fail: password not match")
    public void withdrawal_fail_passwordNotMatch() {
        //given
        String email = "abc@test.com";
        WithdrawalRequest request = new WithdrawalRequest("consumer", "pessword");

        Consumer consumer = new Consumer.Builder(
                email,
                request.getName(),
                EncryptUtil.encode("password"),
                "01012345678",
                new AdsReceive(true, true, true)
        ).build();

        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.of(consumer));

        //when
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> consumerService.withdrawal(email, request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.UNAUTHORIZED);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.INCORRECT_PWD);
    }

    @Test
    @DisplayName("withdrawal success")
    public void withdrawal_success() {
        //given
        String email = "abc@test.com";
        WithdrawalRequest request = new WithdrawalRequest("consumer", "password");

        Consumer consumer = new Consumer.Builder(
                email,
                request.getName(),
                EncryptUtil.encode(request.getPassword()),
                "01012345678",
                new AdsReceive(true, true, true)
        ).build();

        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.of(consumer));

        //when
        Boolean result = consumerService.withdrawal(email, request);

        //then
        assertThat(result).isTrue();
        verify(consumerRepository, times(1)).delete(consumer);
    }
}
