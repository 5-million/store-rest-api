package xyz.fm.storerestapi.dto.user;

import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.util.PhoneNumberUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class PhoneNumberCheckRequest {

    @NotBlank(message = "휴대폰 번호는 " + ErrorDetail.NOT_BLANK)
    @Pattern(regexp = PhoneNumberUtil.PHONE_REG_EXP, message = ErrorDetail.NOT_PHONE_NUMBER_FORMAT)
    private String phoneNumber;

    public PhoneNumberCheckRequest() {/* empty */}

    public PhoneNumberCheckRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
