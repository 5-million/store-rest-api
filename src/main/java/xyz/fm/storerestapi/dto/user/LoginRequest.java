package xyz.fm.storerestapi.dto.user;

import xyz.fm.storerestapi.error.ErrorMessage;

import javax.validation.constraints.Email;

public class LoginRequest {

    @Email(message = ErrorMessage.INVALID_FORMAT)
    private String email;
    private String password;

    public LoginRequest() {/* empty */}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
