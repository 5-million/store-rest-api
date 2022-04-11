package xyz.fm.storerestapi.dto.user.vendor;

import java.util.ArrayList;
import java.util.List;

public class VendorList {

    List<VendorInfo> vendorList;

    public VendorList(List<VendorInfo> vendorList) {
        this.vendorList = new ArrayList<>(vendorList);
    }

    public List<VendorInfo> getVendorList() {
        return vendorList;
    }
}
