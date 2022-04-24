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
import xyz.fm.storerestapi.error.exception.NotFoundException;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ShippingAddressServiceDesignateTest extends ShippingAddressServiceTest {

    private Long defaultShippingAddressId = 11L;
    private Consumer consumer;

    @BeforeEach
    void beforeEach() {
        consumer = new Consumer.Builder(
                "consumer@test.com",
                "consumer",
                "pwd",
                "01012345678",
                new AdsReceive(true, true, true)
        ).build();

        for (int i = 1; i <= 10; i++) {
            consumer.addShippingAddress(
                    new ShippingAddress.Builder(
                            new Address("zipcode" + i, "address" + i, "detailedAddress" + i)
                    ).id((long) i).build()
            );
        }

        consumer.addShippingAddress(
                new ShippingAddress.Builder(
                        new Address("zipcode11", "address11", "detailedAddress11")
                ).id(defaultShippingAddressId).defaultAddress(true).build()
        );
    }

    @Test
    @DisplayName("designateDefaultShippingAddress fail: not found shipping address")
    public void designateDefaultShippingAddress_fail_notFoundShippingAddress() throws Exception {
        //given
        Long shippingAddressId = 0L;

        //when
        NotFoundException exception =
                assertThrows(NotFoundException.class, () -> shippingAddressService.designateDefaultShippingAddress(consumer, shippingAddressId));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS);
    }

    @Test
    @DisplayName("designateDefaultShippingAddress success")
    public void designateDefaultShippingAddress_success() throws Exception {
        //given
        Long shippingAddressId = 5L;

        //when
        shippingAddressService.designateDefaultShippingAddress(consumer, shippingAddressId);

        //then
        for (ShippingAddress shippingAddress : consumer.getShippingAddresses()) {
            if (Objects.equals(shippingAddress.getId(), shippingAddressId)) {
                assertThat(shippingAddress.isDefaultAddress()).isTrue();
            } else {
                assertThat(shippingAddress.isDefaultAddress()).isFalse();
            }
        }
    }
}
