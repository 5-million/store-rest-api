package xyz.fm.storerestapi.dto.user.vendor;

import xyz.fm.storerestapi.entity.user.vendor.VendorManager;

public class VendorManagerLoginResponse extends VendorManagerInfo{

    private Long vendorId;
    private String vendorName;

    public VendorManagerLoginResponse(VendorManager manager) {
        super(
                manager.getId(),
                manager.getEmail(),
                manager.getName(),
                manager.getPhoneNumber(),
                manager.getRole(),
                manager.getPermission()
        );

        this.vendorId = manager.getVendor().getId();
        this.vendorName = manager.getVendor().getVendorName();
    }

    public Long getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }
}
