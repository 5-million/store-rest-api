package xyz.fm.storerestapi.dto.user;

import xyz.fm.storerestapi.error.ErrorMessageConstant;

import javax.validation.constraints.Email;

public class WithdrawalRequest {

    @Email(message = ErrorMessageConstant.NOT_CORRECT_FORMAT)
    private String email;
    private String name;
    private String password;

    public WithdrawalRequest() {/* empty */}

    public WithdrawalRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}