package xyz.fm.storerestapi.dto.vendor;

import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.value.invalid.password.PwdNotEqualToConfirmPwdException;

import javax.validation.Valid;

public class VendorManagerJoinRequest {

    @Valid private Email email;
    private String name;
    @Valid private Phone phone;
    private Password password;
    private Password confirmPassword;
    private Long vendorId;

    public VendorManagerJoinRequest() {/* empty */}

    public VendorManagerJoinRequest(Email email, String name, Phone phone, Password password, Password confirmPassword, Long vendorId) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.vendorId = vendorId;
    }

    public VendorManager toEntity() {
        if (!password.toString().equals(confirmPassword.toString()))
            throw new PwdNotEqualToConfirmPwdException(ErrorCode.PWD_NOT_EQUAL_TO_CONFIRM_PWD);

        return new VendorManager.Builder(
                email,
                name,
                phone,
                password
        ).buildStaff();
    }

    public Email getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Password getPassword() {
        return password;
    }

    public Password getConfirmPassword() {
        return confirmPassword;
    }

    public Long getVendorId() {
        return vendorId;
    }
}
