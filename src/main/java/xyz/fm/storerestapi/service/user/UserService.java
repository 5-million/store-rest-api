package xyz.fm.storerestapi.service.user;

import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.dto.user.UserJoinRequest;
import xyz.fm.storerestapi.dto.user.WithdrawalRequest;
import xyz.fm.storerestapi.entity.user.BaseUserEntity;

public interface UserService <T extends BaseUserEntity> {

    boolean isExistEmail(String email);
    boolean isExistPhoneNumber(String phoneNumber);
    T join(UserJoinRequest request);
    T login(LoginRequest request);
    T modify(UserJoinRequest request);
    int withdrawal(WithdrawalRequest request);
}
