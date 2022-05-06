package xyz.fm.storerestapi.entity.user;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Email {

    @javax.validation.constraints.Email(message = "It must be email format.")
    private String email;

    protected Email() {/* empty */}

    public Email(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return email;
    }
}
