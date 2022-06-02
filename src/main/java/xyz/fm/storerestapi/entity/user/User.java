package xyz.fm.storerestapi.entity.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.fm.storerestapi.entity.BaseTimeEntity;

import javax.persistence.*;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class User extends BaseTimeEntity {

    @Embedded
    @Column(unique = true)
    private Email email;

    private String name;

    @Embedded
    @Column(unique = true)
    private Phone phone;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Embedded
    private Password password;

    @PostLoad
    public void postLoad() {
        email.postLoad();
        phone.postLoad();
    }

    //== business ==//
    public void encryptPassword(PasswordEncoder passwordEncoder) {
        password.encrypt(passwordEncoder);
    }
}
