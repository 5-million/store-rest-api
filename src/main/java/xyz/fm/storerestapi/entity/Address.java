package xyz.fm.storerestapi.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {

    private String zipcode;
    private String base;
    private String detail;

    @Override
    public String toString() {
        return "Address(" + "zipcode: " + zipcode +
                ", base: " + base +
                ", detail: " + detail +
                ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Address) {
            Address address = (Address) obj;

            if (!this.zipcode.equals(address.zipcode)) return false;
            if (!this.base.equals(address.base)) return false;
            if (!this.detail.equals(address.detail)) return false;

            return true;
        }

        return false;
    }
}
