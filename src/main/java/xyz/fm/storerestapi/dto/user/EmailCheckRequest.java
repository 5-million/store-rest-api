package xyz.fm.storerestapi.dto.user;

import xyz.fm.storerestapi.error.ErrorDetail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmailCheckRequest {

    @NotBlank(message = "이메일은 " + ErrorDetail.NOT_BLANK)
    @Email(message = ErrorDetail.NOT_EMAIL_FORMAT)
    private String email;

    public EmailCheckRequest() {/* empty */}

    public EmailCheckRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
