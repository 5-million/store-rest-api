package xyz.fm.storerestapi.dto.user.vendor;

import xyz.fm.storerestapi.dto.user.CommonUserJoinElement;

import javax.validation.Valid;

public class VendorRegisterRequest {

    private String vendorName;
    private String ceo;
    private String registrationNumber;
    private String location;

    @Valid
    private VendorAdmin admin;

    public VendorRegisterRequest() {/* empty */}

    public VendorRegisterRequest(String vendorName, String ceo, String registrationNumber, String location, VendorAdmin admin) {
        this.vendorName = vendorName;
        this.ceo = ceo;
        this.registrationNumber = registrationNumber;
        this.location = location;
        this.admin = admin;
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

    public VendorAdmin getAdmin() {
        return admin;
    }

    public static class VendorAdmin extends CommonUserJoinElement {

        public VendorAdmin(String email, String name, String password, String confirmPassword, String phoneNumber) {
            super(email, name, password, confirmPassword, phoneNumber);
        }
    }
}
