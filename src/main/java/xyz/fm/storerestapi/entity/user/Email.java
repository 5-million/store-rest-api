package xyz.fm.storerestapi.entity.user;

import javax.persistence.*;

@Embeddable
@Access(AccessType.FIELD)
public class Email {

    public static final String CONSTRAINT_EMAIL_MESSAGE = "It must be email format.";

    @javax.validation.constraints.Email(message = CONSTRAINT_EMAIL_MESSAGE)
    private String email;

    @Transient private String id;
    @Transient private String domain;

    protected Email() {/* empty */}

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

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
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
        if (id == null || domain == null) fillTransientField();

        int halfOfIdLength = id.length() / 2;
        String encryptedId = id.substring(0, halfOfIdLength) + "*".repeat(id.length() - halfOfIdLength);

        return encryptedId + "@" + domain;
    }
}
