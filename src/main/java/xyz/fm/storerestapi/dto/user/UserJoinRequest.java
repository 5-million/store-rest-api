package xyz.fm.storerestapi.dto.user;

public interface UserJoinRequest {

    String getEmail();
    String getName();
    String getPassword();
    String getConfirmPassword();
    String getPhoneNumber();
}
