package xyz.fm.storerestapi.entity.user;

import xyz.fm.storerestapi.entity.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class User extends BaseTimeEntity {

    @Embedded
    @Column(unique = true)
    private Email email;

    private String name;

    @Embedded
    @Column(unique = true)
    private Phone phone;

    @Embedded
    private Password password;

    protected User() {/* empty */}

    protected User(Email email, String name, Phone phone, Password password) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    public Email getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Password getPassword() {
        return password;
    }
}
