package xyz.fm.storerestapi.entity.user.vendor;

import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.*;

import javax.persistence.*;

@Entity
public class VendorManager extends User {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private boolean approved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_manager")
    private VendorManager approvalManager;

    protected VendorManager() {/* empty */}

    private VendorManager(Builder builder) {
        super(builder.email, builder.name, builder.phone, builder.password, builder.role);
        this.id = builder.id;
        this.vendor = builder.vendor;
        this.approved = builder.approved;
        this.approvalManager = builder.approvalManager;
    }

    public Long getId() {
        return id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public boolean isApproved() {
        return approved;
    }

    public VendorManager getApprovalManager() {
        return approvalManager;
    }

    @Override
    public String toString() {
        return "VendorManager(" + "id: " + id +
                ", email: " + getEmail() +
                ", name: " + getName() +
                ", phone: " + getPhone() +
                ", password: " + getPassword() +
                ", role: " + getRole() +
                ", approved: " + approved +
                ", " + vendor +
                ", approvalManager: " + approvalManager +
                ")";
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private final Email email;
        private final String name;
        private final Phone phone;
        private final Password password;
        private Role role;
        private Vendor vendor;
        private boolean approved = false;
        private VendorManager approvalManager;

        public Builder(Email email, String name, Phone phone, Password password) {
            this.email = email;
            this.name = name;
            this.phone = phone;
            this.password = password;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder vendor(Vendor vendor) {
            this.vendor = vendor;
            return this;
        }

        public Builder approved(boolean approved) {
            this.approved = approved;
            return this;
        }

        public Builder approvalManager(VendorManager approvalManager) {
            this.approvalManager = approvalManager;
            return this;
        }

        public VendorManager buildExecutive() {
            this.role = Role.ROLE_VENDOR_EXECUTIVE;
            return new VendorManager(this);
        }

        public VendorManager buildStaff() {
            this.role = Role.ROLE_VENDOR_STAFF;
            return new VendorManager(this);
        }
    }
}
