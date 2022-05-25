package xyz.fm.storerestapi.dto.vendor;

import xyz.fm.storerestapi.entity.Address;
import xyz.fm.storerestapi.entity.Vendor;

public class VendorInfo {

    private Long id;
    private String name;
    private String ceo;
    private String regNumber;
    private Address location;

    public static VendorInfo of(Vendor vendor) {
        VendorInfo info = new VendorInfo();
        info.id = vendor.getId();
        info.name = vendor.getName();
        info.ceo = vendor.getCeo();
        info.regNumber = vendor.getRegNumber();
        info.location = vendor.getLocation();
        return info;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCeo() {
        return ceo;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public Address getLocation() {
        return location;
    }
}
