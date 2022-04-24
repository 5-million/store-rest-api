package xyz.fm.storerestapi.dto.shipping;

import xyz.fm.storerestapi.entity.shipping.ShippingAddress;

import java.util.ArrayList;
import java.util.List;

public class ShippingAddressInfoList {

    List<ShippingAddressInfo> shippingAddresses;

    public ShippingAddressInfoList(List<ShippingAddressInfo> shippingAddresses) {
        this.shippingAddresses = new ArrayList<>(shippingAddresses);
    }

    public List<ShippingAddressInfo> getShippingAddresses() {
        return shippingAddresses;
    }
}
