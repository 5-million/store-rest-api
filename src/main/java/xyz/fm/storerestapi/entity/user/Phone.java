package xyz.fm.storerestapi.entity.user;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

@Embeddable
@Access(AccessType.FIELD)
public class Phone {

    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{4})-?([0-9]{4})$", message = "It must be phone number format.")
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
