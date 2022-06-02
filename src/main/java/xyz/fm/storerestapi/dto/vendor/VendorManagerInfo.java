package xyz.fm.storerestapi.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.fm.storerestapi.entity.user.Role;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VendorManagerInfo {

    private Long vendorManagerId;
    private Long vendorId;
    private String email;
    private String name;
    private String phone;
    private Boolean approved;
    private Long approvalManagerId;
    private Role role;

    public static VendorManagerInfo of(VendorManager vendorManager) {
        VendorManager approvalManager = vendorManager.getApprovalManager();
        Long approvalManagerId = approvalManager == null ? null : approvalManager.getId();

        return new VendorManagerInfo(
                vendorManager.getId(),
                vendorManager.getVendor().getId(),
                vendorManager.getEmail().toString(),
                vendorManager.getName(),
                vendorManager.getPhone().toString(),
                vendorManager.isApproved(),
                approvalManagerId,
                vendorManager.getRole()
        );
    }
}
