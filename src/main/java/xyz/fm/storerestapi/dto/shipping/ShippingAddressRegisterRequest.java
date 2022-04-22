package xyz.fm.storerestapi.dto.shipping;

import javax.validation.constraints.NotBlank;

public class ShippingAddressRegisterRequest {

    @NotBlank
    private String zipcode;

    @NotBlank
    private String address;

    @NotBlank
    private String detailedAddress;
    private Boolean isDefaultAddress;

    public ShippingAddressRegisterRequest() {/* empty */}

    public ShippingAddressRegisterRequest(String zipcode, String address, String detailedAddress) {
        this.zipcode = zipcode;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.isDefaultAddress = false;
    }

    public ShippingAddressRegisterRequest(String zipcode, String address, String detailedAddress, Boolean isDefaultAddress) {
        this.zipcode = zipcode;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.isDefaultAddress = isDefaultAddress;
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

    public Boolean isDefaultAddress() {
        return isDefaultAddress;
    }
}
