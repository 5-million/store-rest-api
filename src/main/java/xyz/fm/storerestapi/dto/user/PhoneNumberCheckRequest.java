package xyz.fm.storerestapi.dto.user;

import xyz.fm.storerestapi.error.ErrorDetail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class PhoneNumberCheckRequest {

    @NotBlank(message = ErrorDetail.NOT_PHONE_NUMBER_FORMAT)
    @NotNull(message = "휴대폰 번호는 " + ErrorDetail.NOT_NULL)
    @Pattern(regexp = "/^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/", message = ErrorDetail.NOT_PHONE_NUMBER_FORMAT)
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
