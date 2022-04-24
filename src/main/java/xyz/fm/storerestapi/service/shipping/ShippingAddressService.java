package xyz.fm.storerestapi.service.shipping;

import xyz.fm.storerestapi.dto.shipping.ShippingAddressModifyRequest;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressRegisterRequest;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;

public interface ShippingAddressService {

    ShippingAddress register(Consumer consumer, ShippingAddressRegisterRequest request);
    ShippingAddress getById(Long shippingAddressId);
    void modify(String consumerEmail, ShippingAddressModifyRequest request);
    void delete(String consumerEmail, Long shippingAddressId);
    void designateDefaultShippingAddress(Consumer consumer, Long shippingAddressId);
}
