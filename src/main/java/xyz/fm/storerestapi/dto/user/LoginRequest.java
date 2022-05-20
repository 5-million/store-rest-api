package xyz.fm.storerestapi.dto.user;

public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest() {/* empty */}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
