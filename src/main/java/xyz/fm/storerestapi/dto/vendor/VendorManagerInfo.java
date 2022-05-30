package xyz.fm.storerestapi.dto.vendor;

import xyz.fm.storerestapi.entity.user.Role;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;

public class VendorManagerInfo {

    private Long vendorManagerId;
    private Long vendorId;
    private String email;
    private String name;
    private String phone;
    private Boolean approved;
    private Long approvalManagerId;
    private Role role;

    public VendorManagerInfo() {/* empty */}

    public static VendorManagerInfo of(Long vendorManagerId, Long vendorId, String email, String name, String phone, Boolean approved, Long approvalManagerId, Role role) {
        VendorManagerInfo info = new VendorManagerInfo();
        info.vendorManagerId = vendorManagerId;
        info.vendorId = vendorId;
        info.email = email;
        info.name = name;
        info.phone = phone;
        info.approved = approved;
        info.approvalManagerId = approvalManagerId;
        info.role = role;

        return info;
    }

    public static VendorManagerInfo of(VendorManager vendorManager) {
        VendorManager approvalManager = vendorManager.getApprovalManager();
        Long approvalManagerId = approvalManager == null ? null : approvalManager.getId();

        return of(
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

    public Long getVendorManagerId() {
        return vendorManagerId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getApproved() {
        return approved;
    }

    public Long getApprovalManagerId() {
        return approvalManagerId;
    }

    public Role getRole() {
        return role;
    }
}
