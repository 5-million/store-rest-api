package xyz.fm.storerestapi.entity.user;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

@Embeddable
@Access(AccessType.FIELD)
public class Phone {

    public static final String CONSTRAINT_PHONE_MESSAGE = "It must be phone number format.";

    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{4})-?([0-9]{4})$", message = CONSTRAINT_PHONE_MESSAGE)
    private String phone;

    protected Phone() {/* empty */}

    public Phone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return phone;
    }
}
