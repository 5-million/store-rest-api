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

    public ShippingAddressInfo(Long consumerId, Long shippingAddressId, String zipcode, String address, String detailedAddress, Boolean isDefaultAddress) {
        this.consumerId = consumerId;
        this.shippingAddressId = shippingAddressId;
        this.zipcode = zipcode;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.isDefaultAddress = isDefaultAddress;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ShippingAddressInfo(");
        sb.append("consumerId: " + consumerId);
        sb.append(", shippingAddressId: " + shippingAddressId);
        sb.append(", zipcode: " + zipcode);
        sb.append(", address: " + address);
        sb.append(", detailedAddress: " + detailedAddress);
        sb.append(", isDefaultAddress: " + isDefaultAddress);
        sb.append(")");
        return sb.toString();
    }
}
