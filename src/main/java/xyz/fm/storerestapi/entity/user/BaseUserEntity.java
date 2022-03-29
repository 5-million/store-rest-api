package xyz.fm.storerestapi.entity.user;

import xyz.fm.storerestapi.entity.BaseTimeEntity;

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
}
