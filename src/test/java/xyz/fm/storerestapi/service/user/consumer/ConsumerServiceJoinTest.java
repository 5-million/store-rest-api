package xyz.fm.storerestapi.service.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.user.UserJoinRequest;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerJoinRequest;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.InvalidPasswordException;
import xyz.fm.storerestapi.error.exception.TypeMismatchException;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class ConsumerServiceJoinTest extends ConsumerServiceTest{

    @Test
    @DisplayName("join fail: not consumer join request")
    public void join_fail_notConsumerJoinRequest() {
        //given
        UserJoinRequest request = new TestUserJoinRequest();

        //when
        TypeMismatchException exception = assertThrows(TypeMismatchException.class, () -> consumerService.join(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.TYPE_MISMATCH);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_CONSUMER_JOIN_REQUEST);
        assertThat(exception.isDetailIgnoreForUser()).isTrue();
    }

    @Test
    @DisplayName("join fail: pwd != confirmPwd")
    public void join_fail_pwdIsNotEqualConfirmPwd() {
        //given
        UserJoinRequest request = new ConsumerJoinRequest(
                "abc@test.com",
                "name",
                "pwd",
                "confirmPwd",
                "01012341234",
                new ConsumerJoinRequest.AdsReceive(true, true, true)
        );

        //when
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> consumerService.join(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.INVALID_PASSWORD);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD);
        assertThat(exception.isDetailIgnoreForUser()).isFalse();
    }

    @Test
    @DisplayName("join fail: 이미 존재하는 소비자")
    public void join_fail_이미_존재하는_소비자() {
        //given
        UserJoinRequest request = new ConsumerJoinRequest(
                "abc@test.com",
                "name",
                "pwd",
                "pwd",
                "01012341234",
                new ConsumerJoinRequest.AdsReceive(true, true, true)
        );

        given(consumerRepository.save(any(Consumer.class))).willThrow(PersistenceException.class);

        //when
        DuplicationException exception = assertThrows(
                DuplicationException.class,
                () -> consumerService.join(request)
        );

        //then
        assertThat(exception.getError()).isEqualTo(Error.DUPLICATE);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.DUPLICATE_USER);
        assertThat(exception.isDetailIgnoreForUser()).isFalse();
    }

    @Test
    @DisplayName("join success")
    public void join_success() {
        //given
        ConsumerJoinRequest request = new ConsumerJoinRequest(
                "abc@test.com",
                "name",
                "pwd",
                "pwd",
                "01012341234",
                new ConsumerJoinRequest.AdsReceive(true, true, true)
        );

        given(consumerRepository.save(any(Consumer.class))).willReturn(
                new Consumer.Builder(
                        "abc@test.com",
                        "name",
                        "pwd",
                        "01012341234",
                        new AdsReceive(true, true, true)
                )
                        .id(1L)
                        .build()
        );

        //when
        Consumer result = consumerService.join(request);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
        assertThat(result.getName()).isEqualTo(request.getName());
        assertThat(result.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        assertThat(result.getReserves()).isEqualTo(0L);

        assertThat(result.getAdsReceive().getToEmail()).isEqualTo(request.getAdsReceive().getToEmail());
        assertThat(result.getAdsReceive().getToSMSAndMMS()).isEqualTo(request.getAdsReceive().getToSMSAndMMS());
        assertThat(result.getAdsReceive().getToAppPush()).isEqualTo(request.getAdsReceive().getToAppPush());
    }

    private class TestUserJoinRequest implements UserJoinRequest {
        @Override
        public String getEmail() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public String getConfirmPassword() {
            return null;
        }

        @Override
        public String getPhoneNumber() {
            return null;
        }
    }
}
