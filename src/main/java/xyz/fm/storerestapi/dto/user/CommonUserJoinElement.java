package xyz.fm.storerestapi.dto.user;

import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.util.PhoneNumberUtil;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public abstract class CommonUserJoinElement implements UserJoinRequest {

    @NotBlank(message = "이메일은 " + ErrorDetail.NOT_BLANK)
    @Email(message = ErrorDetail.NOT_EMAIL_FORMAT)
    protected String email;

    protected String name;
    protected String password;
    protected String confirmPassword;

    @NotBlank(message = "휴대폰 번호는 " + ErrorDetail.NOT_BLANK)
    @Pattern(regexp = PhoneNumberUtil.PHONE_REG_EXP, message = ErrorDetail.NOT_PHONE_NUMBER_FORMAT)
    protected String phoneNumber;

    protected CommonUserJoinElement() {/* empty */}

    protected CommonUserJoinElement(String email, String name, String password, String confirmPassword, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isValidPassword() {
        return password.equals(confirmPassword);
    }
}
