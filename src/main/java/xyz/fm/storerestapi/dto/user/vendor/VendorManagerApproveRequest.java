package xyz.fm.storerestapi.dto.user.vendor;

import java.util.List;

public class VendorManagerApproveRequest {

    List<String> target;

    public VendorManagerApproveRequest() {/* empty */}

    public VendorManagerApproveRequest(List<String> target) {
        this.target = target;
    }

    public List<String> getTarget() {
        return target;
    }
}
