package xyz.fm.storerestapi.entity.user.vendor;

public enum VendorRole {

    VENDOR_ROOT("ROOT"),
    VENDOR_MANAGER("MANAGER"),
    ;

    private final String role;

    VendorRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static VendorRole of(String role) {
        for (VendorRole vendorRole : values()) {
            if (vendorRole.role.equals(role)) {
                return vendorRole;
            }
        }

        return null;
    }
}
