package xyz.fm.storerestapi.service.shipping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.entity.shipping.Address;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotEntityOwnerException;
import xyz.fm.storerestapi.error.exception.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ShippingAddressServiceDeleteTest extends ShippingAddressServiceTest {

    private Consumer consumer;
    private ShippingAddress shippingAddress;

    @BeforeEach
    void beforeEach() {
        consumer = new Consumer.Builder(
                "consumer@test.com",
                "consumer",
                "password",
                "01012345678",
                new AdsReceive(true, false, true)
        ).id(1L).build();

        shippingAddress = new ShippingAddress.Builder(
                new Address("zipcode", "address", "detailedAddress")
        ).consumer(consumer).id(2L).build();
    }

    @Test
    @DisplayName("delete fail: not found shipping address")
    public void delete_fail_notFoundShippingAddress() throws Exception {
        //given
        String consumerEmail = shippingAddress.getConsumer().getEmail();
        Long shippingAddressId = shippingAddress.getId();

        given(shippingAddressRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        NotFoundException exception =
                assertThrows(NotFoundException.class, () -> shippingAddressService.delete(consumerEmail, shippingAddressId));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS);
    }

    @Test
    @DisplayName("delete fail: not entity owner")
    public void delete_fail_notEntityOwner() throws Exception {
        //given
        String consumerEmail = "requester@test.com";
        Long shippingAddressId = shippingAddress.getId();

        given(shippingAddressRepository.findById(anyLong())).willReturn(Optional.of(shippingAddress));

        //when
        NotEntityOwnerException exception =
                assertThrows(NotEntityOwnerException.class, () -> shippingAddressService.delete(consumerEmail, shippingAddressId));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NO_PERMISSION);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_ENTITY_OWNER);
    }

    @Test
    @DisplayName("delete success")
    public void delete_success() throws Exception {
        //given
        String consumerEmail = shippingAddress.getConsumer().getEmail();
        Long shippingAddressId = shippingAddress.getId();

        given(shippingAddressRepository.findById(anyLong())).willReturn(Optional.of(shippingAddress));

        //when
        shippingAddressService.delete(consumerEmail, shippingAddressId);

        //then
        verify(shippingAddressRepository, times(1)).delete(any(ShippingAddress.class));
    }
}
