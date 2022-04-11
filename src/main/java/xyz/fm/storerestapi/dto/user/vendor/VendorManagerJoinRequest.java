package xyz.fm.storerestapi.dto.user.vendor;

import xyz.fm.storerestapi.dto.user.CommonUserJoinElement;

public class VendorManagerJoinRequest extends CommonUserJoinElement {

    private Long vendorId;

    public VendorManagerJoinRequest() {/* empty */}

    public VendorManagerJoinRequest(Long vendorId, String email, String name, String password, String confirmPassword, String phoneNumber) {
        super(email, name, password, confirmPassword, phoneNumber);
        this.vendorId = vendorId;
    }

    public Long getVendorId() {
        return vendorId;
    }
}
