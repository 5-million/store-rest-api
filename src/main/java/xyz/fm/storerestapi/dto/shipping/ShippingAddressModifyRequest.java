package xyz.fm.storerestapi.dto.shipping;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ShippingAddressModifyRequest extends ShippingAddressRegisterRequest {

    @JsonIgnore
    private Long shippingAddressId;

    public ShippingAddressModifyRequest() {/* empty */}

    public ShippingAddressModifyRequest(String zipcode, String address, String detailedAddress) {
        super(zipcode, address, detailedAddress);
    }

    public ShippingAddressModifyRequest(String zipcode, String address, String detailedAddress, Boolean isDefaultAddress) {
        super(zipcode, address, detailedAddress, isDefaultAddress);
    }

    public Long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }
}
