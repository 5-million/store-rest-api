package xyz.fm.storerestapi.dto.user.vendor;

import java.util.ArrayList;
import java.util.List;

public class VendorManagerList {

    List<VendorManagerInfo> vendorManagerList;

    public VendorManagerList(List<VendorManagerInfo> vendorManagerList) {
        this.vendorManagerList = new ArrayList<>(vendorManagerList);
    }

    public List<VendorManagerInfo> getVendorManagerList() {
        return vendorManagerList;
    }
}
