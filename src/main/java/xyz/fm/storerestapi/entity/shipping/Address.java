package xyz.fm.storerestapi.entity.shipping;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Address {

    private String zipcode;
    private String address;
    private String detailedAddress;

    protected Address() {/* empty*/}

    public Address(String zipcode, String address, String detailedAddress) {
        this.zipcode = zipcode;
        this.address = address;
        this.detailedAddress = detailedAddress;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getAddress() {
        return address;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Address(");
        sb.append("zipcode: " + zipcode);
        sb.append(", address: " + address);
        sb.append(", detailedAddress: " + detailedAddress);
        sb.append(")");
        return sb.toString();
    }
}
