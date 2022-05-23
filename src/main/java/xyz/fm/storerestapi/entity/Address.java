package xyz.fm.storerestapi.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Address {

    private String zipcode;
    private String base;
    private String detail;

    protected Address() {/* empty */}

    public Address(String zipcode, String base, String detail) {
        this.zipcode = zipcode;
        this.base = base;
        this.detail = detail;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getBase() {
        return base;
    }

    public String getDetail() {
        return detail;
    }

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
