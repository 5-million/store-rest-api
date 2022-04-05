package xyz.fm.storerestapi.entity.user;

import xyz.fm.storerestapi.entity.BaseTimeEntity;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;
import xyz.fm.storerestapi.util.EncryptUtil;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseUserEntity extends BaseTimeEntity {

    private String email;
    private String name;
    private String password;
    private String phoneNumber;
    private LocalDateTime lastLoginDate;

    protected BaseUserEntity() {/* empty */}

    protected BaseUserEntity(String email, String name, String password, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    //== business ==//
    public boolean login(String plainTextPassword) {
        if (isMatchedPassword(plainTextPassword)) {
            updateLastLoginDate();
            return true;
        } else return false;
    }

    public boolean isMatchedPassword(String plainTextPassword) {
        return EncryptUtil.match(plainTextPassword, password);
    }

    public boolean isMatchedName(String name) {
        return this.name.equals(name);
    }

    public void changePassword(String oldPlainTextPassword, String newPlainTextPassword) {
        if (isMatchedPassword(oldPlainTextPassword)) {
            this.password = EncryptUtil.encode(newPlainTextPassword);
        } else {
            throw new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.INCORRECT_PWD);
        }
    }

    private void updateLastLoginDate() {
        lastLoginDate = LocalDateTime.now();
    }
}
