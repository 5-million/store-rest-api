package xyz.fm.storerestapi.service.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.CustomException;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicateEmailException;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicatePhoneException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConsumerServiceJoinTest extends ConsumerServiceTest {

    private Consumer consumer;

    @BeforeEach
    void beforeEach() {
        consumer = new Consumer.Builder(
                new Email("test@test.com"),
                "test",
                new Phone("01012345678"),
                new Password("password")
        ).id(1L).build();
    }

    @Test
    void joinWillThrowDuplicateEmailEx() {
        //given
        given(consumerRepository.existsByEmail(any(Email.class))).willReturn(true);

        //when
        DuplicateEmailException thrown = assertThrows(DuplicateEmailException.class, () -> join(consumer));

        //then
        assertErrorCode(thrown, ErrorCode.DUPLICATE_EMAIL);
    }

    private void assertErrorCode(CustomException thrown, ErrorCode errorCode) {
        assertThat(thrown.getErrorCode()).isEqualTo(errorCode);
    }

    private Consumer join(Consumer consumer) {
        return consumerService.join(consumer);
    }

    @Test
    void joinWillThrowDuplicatePhoneEx() {
        //given
        given(consumerRepository.findByPhone(any(Phone.class))).willReturn(Optional.of(consumer));

        //when
        DuplicatePhoneException thrown = assertThrows(DuplicatePhoneException.class, () -> join(consumer));

        //then
        assertErrorCode(thrown, ErrorCode.DUPLICATE_PHONE);
        assertThat(thrown.getRegisteredEmail()).isEqualTo(consumer.getEmail().encrypt());
    }

    @Test
    void joinSuccess() {
        //given
        given(consumerRepository.save(any(Consumer.class))).willReturn(consumer);

        //when
        Consumer result = join(consumer);

        //then
        verify(consumerRepository, times(1)).save(any(Consumer.class));
        verify(passwordEncoder, times(1)).encode(any());
        assertThat(result).isEqualTo(consumer);
    }
}
