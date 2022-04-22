package xyz.fm.storerestapi.dto.shipping;

import xyz.fm.storerestapi.entity.shipping.ShippingAddress;

public class ShippingAddressInfo {

    private Long consumerId;
    private Long shippingAddressId;
    private String zipcode;
    private String address;
    private String detailedAddress;
    private Boolean isDefaultAddress;

    public ShippingAddressInfo(ShippingAddress shippingAddress) {
        this.consumerId = shippingAddress.getConsumer().getId();
        this.shippingAddressId = shippingAddress.getId();
        this.zipcode = shippingAddress.getAddress().getZipcode();
        this.address = shippingAddress.getAddress().getAddress();
        this.detailedAddress = shippingAddress.getAddress().getDetailedAddress();
        this.isDefaultAddress = shippingAddress.isDefaultAddress();
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public Long getShippingAddressId() {
        return shippingAddressId;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getAddress() {
        return address;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public Boolean getIsDefaultAddress() {
        return isDefaultAddress;
    }
}
