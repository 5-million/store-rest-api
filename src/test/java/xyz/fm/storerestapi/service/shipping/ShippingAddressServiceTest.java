package xyz.fm.storerestapi.service.shipping;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;

@ExtendWith(MockitoExtension.class)
abstract class ShippingAddressServiceTest {

    protected Consumer consumer = new Consumer.Builder(
            "consumer@test.com",
            "consumer",
            "pwd",
            "01012345678",
            new AdsReceive(true, true, true)
    ).build();

    @InjectMocks protected ShippingAddressServiceImpl shippingAddressService;

}