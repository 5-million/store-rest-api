package xyz.fm.storerestapi.dto.user;

import xyz.fm.storerestapi.error.ErrorDetail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EmailCheckRequest {

    @NotBlank(message = ErrorDetail.NOT_EMAIL_FORMAT)
    @NotNull(message = "이메일은 " + ErrorDetail.NOT_NULL)
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
