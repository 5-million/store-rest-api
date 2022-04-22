package xyz.fm.storerestapi.service.shipping;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressModifyRequest;
import xyz.fm.storerestapi.entity.shipping.Address;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotEntityOwnerException;
import xyz.fm.storerestapi.error.exception.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

public class ShippingAddressServiceModifyTest extends ShippingAddressServiceTest {

    @Test
    @DisplayName("modify fail: not owner")
    public void modify_fail_notOwner() throws Exception {
        //given
        String requesterEmail = "modify@test.com";
        ShippingAddress originShippingAddress = new ShippingAddress.Builder(
                new Address("00000", "origin address", "origin detailed address")
        ).consumer(consumer).id(2L).build();

        ShippingAddressModifyRequest request =
                new ShippingAddressModifyRequest("11111", "updated address", "updated detailed address");
        request.setShippingAddressId(originShippingAddress.getId());

        given(shippingAddressRepository.findById(anyLong())).willReturn(Optional.of(originShippingAddress));

        //when
        NotEntityOwnerException exception =
                assertThrows(NotEntityOwnerException.class, () -> shippingAddressService.modify(requesterEmail, request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NO_PERMISSION);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_ENTITY_OWNER);
    }

    @Test
    @DisplayName("modify fail: not found shipping address")
    public void modify_fail_notFoundShippingAddress() throws Exception {
        //given
        String requesterEmail = "modify@test.com";
        ShippingAddressModifyRequest request =
                new ShippingAddressModifyRequest("11111", "updated address", "updated detailed address");
        request.setShippingAddressId(2L);

        given(shippingAddressRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        NotFoundException exception =
                assertThrows(NotFoundException.class, () -> shippingAddressService.modify(requesterEmail, request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_SHIPPING_ADDRESS);
    }

    @Test
    @DisplayName("modify success")
    public void modify_success() throws Exception {
        //given
        ShippingAddress originShippingAddress = new ShippingAddress.Builder(
                new Address("00000", "origin address", "origin detailed address")
        ).consumer(consumer).id(2L).build();

        ShippingAddressModifyRequest request =
                new ShippingAddressModifyRequest("11111", "updated address", "updated detailed address", true);
        request.setShippingAddressId(originShippingAddress.getId());

        given(shippingAddressRepository.findById(anyLong())).willReturn(Optional.of(originShippingAddress));

        //when
        shippingAddressService.modify(consumer.getEmail(), request);

        //then
        assertThat(originShippingAddress.getAddress().getZipcode()).isEqualTo(request.getZipcode());
        assertThat(originShippingAddress.getAddress().getAddress()).isEqualTo(request.getAddress());
        assertThat(originShippingAddress.getAddress().getDetailedAddress()).isEqualTo(request.getDetailedAddress());
        assertThat(originShippingAddress.isDefaultAddress()).isEqualTo(request.isDefaultAddress());
    }
}
