package xyz.fm.storerestapi.dto.user.vendor;

import xyz.fm.storerestapi.entity.user.vendor.Vendor;

public class VendorInfo {

    private Long vendorId;
    private String vendorName;
    private String ceo;
    private String registrationNumber;
    private String location;

    public VendorInfo(Long id, String vendorName, String ceo, String registrationNumber, String location) {
        this.vendorId = id;
        this.vendorName = vendorName;
        this.ceo = ceo;
        this.registrationNumber = registrationNumber;
        this.location = location;
    }

    public static VendorInfo of(Vendor vendor) {
        return new VendorInfo(
                vendor.getId(),
                vendor.getVendorName(),
                vendor.getCeo(),
                vendor.getRegistrationNumber(),
                vendor.getLocation()
        );
    }

    public Long getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getCeo() {
        return ceo;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getLocation() {
        return location;
    }
}
