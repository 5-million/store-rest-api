package xyz.fm.storerestapi.entity.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

@Embeddable
@Access(AccessType.FIELD)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phone {

    public static final String CONSTRAINT_PHONE_MESSAGE = "It must be phone number format.";

    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{4})-?([0-9]{4})$", message = CONSTRAINT_PHONE_MESSAGE)
    private String phone;

    @Transient private String first;
    @Transient private String middle;
    @Transient private String last;

    public Phone(String phone) {
        this.phone = phone;
        fillTransientField();
    }

    protected void postLoad() {
        fillTransientField();
    }

    private void fillTransientField() {
        if (phone.length() == 13) {
            String[] split = phone.split("-");
            this.first = split[0];
            this.middle = split[1];
            this.last = split[2];
        } else {
            this.first = phone.substring(0, 3);
            this.middle = phone.substring(3, 7);
            this.last = phone.substring(7, 11);
            phone = first + "-" + middle + "-" + last;
        }
    }

    @Override
    public String toString() {
        return phone;
    }
}
