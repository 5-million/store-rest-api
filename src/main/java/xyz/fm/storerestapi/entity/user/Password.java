package xyz.fm.storerestapi.entity.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Password {

    private String password;

    protected Password() {/* empty */}

    public Password(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return password;
    }

    //== business ==//
    public void encrypt(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }
}
