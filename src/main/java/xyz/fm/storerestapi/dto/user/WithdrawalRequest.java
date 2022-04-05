package xyz.fm.storerestapi.dto.user;

public class WithdrawalRequest {

    private String name;
    private String password;

    public WithdrawalRequest() {/* empty */}

    public WithdrawalRequest(String name, String password) {
        this.name = name;
        this.password = password;
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
