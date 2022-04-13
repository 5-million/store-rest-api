package xyz.fm.storerestapi.dto.user;

public class MyInfoRequest {

    private String password;

    public MyInfoRequest() {/* empty */}

    public MyInfoRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
