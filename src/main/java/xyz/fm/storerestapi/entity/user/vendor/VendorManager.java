package xyz.fm.storerestapi.entity.user.vendor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.*;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.nopermission.VendorManagerAuthorityException;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private VendorManager(Email email, String name, Phone phone, Role role, Password password, Long id, Vendor vendor, boolean approved, VendorManager approvalManager) {
        super(email, name, phone, role, password);
        this.id = id;
        this.vendor = vendor;
        this.approved = approved;
        this.approvalManager = approvalManager;
    }

    //== business ==//
    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public void approve(VendorManager manager) {
        if (this.getRole() == Role.ROLE_VENDOR_STAFF)
            throwVendorManagerAuthorityException(ErrorCode.REQUIRE_MORE_THEN_EXECUTIVE_ROLE);

        if (!this.approved)
            throwVendorManagerAuthorityException(ErrorCode.NOT_APPROVED_VENDOR_MANAGER);

        if (this.vendor != manager.getVendor())
            throwVendorManagerAuthorityException(ErrorCode.NOT_SAME_VENDOR);

        manager.approvalManager = this;
        manager.approved = true;
    }

    private void throwVendorManagerAuthorityException(ErrorCode errorCode) {
        throw new VendorManagerAuthorityException(errorCode);
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
}
