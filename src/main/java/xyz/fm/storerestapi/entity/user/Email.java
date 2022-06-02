package xyz.fm.storerestapi.entity.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Embeddable
@Access(AccessType.FIELD)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    public static final String CONSTRAINT_EMAIL_MESSAGE = "It must be email format.";

    @javax.validation.constraints.Email(message = CONSTRAINT_EMAIL_MESSAGE)
    private String email;

    @Transient private String id;
    @Transient private String domain;

    public Email(String email) {
        this.email = email;
//        fillTransientField();
    }

    protected void postLoad() {
        fillTransientField();
    }

    private void fillTransientField() {
        String[] split = email.split("@");
        this.id = split[0];
        this.domain = split[1];
    }

    @Override
    public String toString() {
        return email;
    }

    //== business ==//
    public String encrypt() {
        if (id == null || domain == null) fillTransientField();

        int halfOfIdLength = id.length() / 2;
        String encryptedId = id.substring(0, halfOfIdLength) + "*".repeat(id.length() - halfOfIdLength);

        return encryptedId + "@" + domain;
    }
}
