package xyz.fm.storerestapi.dto.vendor;

import xyz.fm.storerestapi.entity.Address;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.value.invalid.password.PwdNotEqualToConfirmPwdException;

import javax.validation.Valid;

public class VendorRegisterRequest {

    private String name;
    private String regNumber;
    private String ceo;
    private Address location;
    @Valid private VendorExecutive executive;

    public VendorRegisterRequest() {/* empty */}

    public VendorRegisterRequest(String name, String regNumber, String ceo, Address location, VendorExecutive executive) {
        this.name = name;
        this.regNumber = regNumber;
        this.ceo = ceo;
        this.location = location;
        this.executive = executive;
    }

    public Vendor toEntity() {
        return new Vendor.Builder(name, regNumber, ceo, location).build();
    }

    public String getName() {
        return name;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getCeo() {
        return ceo;
    }

    public Address getLocation() {
        return location;
    }

    public VendorExecutive getExecutive() {
        return executive;
    }

    public static class VendorExecutive {
        @Valid private Email email;
        private String name;
        @Valid private Phone phone;
        private Password password;
        private Password confirmPassword;

        public VendorExecutive() {/* empty */}

        public VendorExecutive(Email email, String name, Phone phone, Password password, Password confirmPassword) {
            this.email = email;
            this.name = name;
            this.phone = phone;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }

        public VendorManager toEntity() {
            if (!password.toString().equals(confirmPassword.toString()))
                throw new PwdNotEqualToConfirmPwdException(ErrorCode.PWD_NOT_EQUAL_TO_CONFIRM_PWD);

            return new VendorManager.Builder(email, name, phone, password)
                    .approved(true)
                    .buildExecutive();
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
    }
}
