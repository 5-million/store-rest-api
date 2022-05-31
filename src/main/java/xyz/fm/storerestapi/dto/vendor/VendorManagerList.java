package xyz.fm.storerestapi.dto.vendor;

import java.util.List;

public class VendorManagerList {

    private List<VendorManagerInfo> vendorManagerList;

    public VendorManagerList(List<VendorManagerInfo> vendorManagerList) {
        this.vendorManagerList = vendorManagerList;
    }

    public List<VendorManagerInfo> getVendorManagerList() {
        return vendorManagerList;
    }
}
