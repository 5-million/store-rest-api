package xyz.fm.storerestapi.entity.user;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Email {

    public static final String CONSTRAINT_EMAIL_MESSAGE = "It must be email format.";

    @javax.validation.constraints.Email(message = CONSTRAINT_EMAIL_MESSAGE)
    private String email;

    private String domain;

    protected Email() {/* empty */}

    public Email(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getDomain() {
        return domain;
    }

    @Override
    public String toString() {
        return email;
    }

    //== business ==//
    public String encrypt() {
        String[] split = this.email.split("@");
        String id = split[0];
        String domain = split[1];

        int halfOfIdLength = id.length() / 2;
        String encryptedId = id.substring(0, halfOfIdLength) + "*".repeat(id.length() - halfOfIdLength);

        return encryptedId + "@" + domain;
    }
}
