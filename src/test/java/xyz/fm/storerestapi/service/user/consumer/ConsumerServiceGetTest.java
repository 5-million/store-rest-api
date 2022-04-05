package xyz.fm.storerestapi.service.user.consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class ConsumerServiceGetTest extends ConsumerServiceTest {

    @Test
    @DisplayName("getById fail: not found user")
    public void getById_fail_notFoundUser() {
        //given
        String email = "abc@test.com";
        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> consumerService.getByEmail(email));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_USER);
    }

    @Test
    @DisplayName("getById success")
    public void getById_success() {
        //given
        Consumer consumer = new Consumer.Builder(
                "abc@test.com",
                "name",
                "pwd",
                "01012341234",
                new AdsReceive(true, true, true)
        )
                .id(1L)
                .build();

        given(consumerRepository.findByEmail(anyString())).willReturn(Optional.of(consumer));

        //when
        Consumer findConsumer = consumerService.getByEmail(consumer.getEmail());

        //then
        assertThat(consumer).isEqualTo(findConsumer);
    }
}
