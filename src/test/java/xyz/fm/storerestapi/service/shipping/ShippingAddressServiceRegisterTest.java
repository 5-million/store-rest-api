package xyz.fm.storerestapi.service.shipping;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressRegisterRequest;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;

import static org.assertj.core.api.Assertions.assertThat;

public class ShippingAddressServiceRegisterTest extends ShippingAddressServiceTest {
    
    @Test
    @DisplayName("register success: set default address")
    public void register_success_defaultAddress() throws Exception {
        //given
        ShippingAddressRegisterRequest request = new ShippingAddressRegisterRequest(
                "05510", "서울특별시 송파구 송파대로 570", "8~26층", true
        );

        //when
        ShippingAddress result = shippingAddressService.register(consumer, request);

        //then
        assertThat(result.getConsumer()).isEqualTo(consumer);
        assertThat(result.getAddress().getZipcode()).isEqualTo(request.getZipcode());
        assertThat(result.getAddress().getAddress()).isEqualTo(request.getAddress());
        assertThat(result.getAddress().getDetailedAddress()).isEqualTo(request.getDetailedAddress());
        assertThat(result.isDefaultAddress()).isTrue();
    }

    @Test
    @DisplayName("register success: not default address")
    public void register_success_notDefaultAddress() throws Exception {
        //given
        ShippingAddressRegisterRequest request = new ShippingAddressRegisterRequest(
                "05510", "서울특별시 송파구 송파대로 570", "8~26층"
        );

        //when
        ShippingAddress result = shippingAddressService.register(consumer, request);

        //then
        assertThat(result.getConsumer()).isEqualTo(consumer);
        assertThat(result.getAddress().getZipcode()).isEqualTo(request.getZipcode());
        assertThat(result.getAddress().getAddress()).isEqualTo(request.getAddress());
        assertThat(result.getAddress().getDetailedAddress()).isEqualTo(request.getDetailedAddress());
        assertThat(result.isDefaultAddress()).isFalse();
    }
}
