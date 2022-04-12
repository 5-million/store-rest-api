package xyz.fm.storerestapi.dto.user.vendor;

import xyz.fm.storerestapi.entity.user.vendor.VendorRole;

public class VendorManagerInfo {
    private Long managerId;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private Boolean permission;

    public VendorManagerInfo(Long id, String email, String name, String phoneNumber, VendorRole role, Boolean permission) {
        this.managerId = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role.getRole();
        this.permission = permission;
    }

    public Long getManagerId() {
        return managerId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public Boolean getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("VendorManagerInfo(");
        sb.append("email: ").append(email);
        sb.append(" name: ").append(name);
        sb.append(" phoneNumber: ").append(phoneNumber);
        sb.append(" permission: ").append(permission);
        sb.append(")");
        return sb.toString();
    }
}