package xyz.fm.storerestapi.entity.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Password {

    private String password;

    @Override
    public String toString() {
        return password;
    }

    //== business ==//
    public void encrypt(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }
}
