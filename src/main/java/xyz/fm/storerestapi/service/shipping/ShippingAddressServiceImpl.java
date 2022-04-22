package xyz.fm.storerestapi.service.shipping;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressRegisterRequest;
import xyz.fm.storerestapi.entity.shipping.Address;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;

@Service
@Transactional
public class ShippingAddressServiceImpl implements ShippingAddressService {

    @Override
    public ShippingAddress register(Consumer consumer, ShippingAddressRegisterRequest request) {
        Address address = new Address(request.getZipcode(), request.getAddress(), request.getDetailedAddress());
        ShippingAddress shippingAddress = new ShippingAddress.Builder(address)
                .defaultAddress(request.isDefaultAddress())
                .build();

        consumer.addShippingAddress(shippingAddress);
        return shippingAddress;
    }
}
