package xyz.fm.storerestapi.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.fm.storerestapi.entity.Address;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.Role;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.value.invalid.password.PwdNotEqualToConfirmPwdException;

import javax.validation.Valid;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VendorRegisterRequest {

    private String name;
    private String regNumber;
    private String ceo;
    private Address location;
    @Valid private VendorExecutive executive;

    public Vendor toEntity() {
        return Vendor.builder()
                .name(name)
                .regNumber(regNumber)
                .ceo(ceo)
                .location(location)
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorExecutive {
        @Valid private Email email;
        private String name;
        @Valid private Phone phone;
        private Password password;
        private Password confirmPassword;

        public VendorManager toEntity() {
            if (!password.toString().equals(confirmPassword.toString()))
                throw new PwdNotEqualToConfirmPwdException(ErrorCode.PWD_NOT_EQUAL_TO_CONFIRM_PWD);

            return VendorManager.builder()
                    .email(email)
                    .name(name)
                    .phone(phone)
                    .password(password)
                    .approved(true)
                    .role(Role.ROLE_VENDOR_EXECUTIVE)
                    .build();
        }
    }
}
