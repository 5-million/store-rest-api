package xyz.fm.storerestapi.service.shipping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.repository.shipping.ShippingAddressRepository;

@ExtendWith(MockitoExtension.class)
abstract class ShippingAddressServiceTest {

    protected Consumer consumer;

    @BeforeEach
    void beforeEach() {
        consumer = new Consumer.Builder(
                "consumer@test.com",
                "consumer",
                "pwd",
                "01012345678",
                new AdsReceive(true, true, true)
        ).build();
    }

    @Mock protected ShippingAddressRepository shippingAddressRepository;
    @InjectMocks protected ShippingAddressServiceImpl shippingAddressService;

}