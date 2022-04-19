package xyz.fm.storerestapi.entity.user.vendor;

import xyz.fm.storerestapi.entity.user.BaseUserEntity;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.LoginException;
import xyz.fm.storerestapi.error.exception.NoPermissionException;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "STORE_VENDOR_MANAGER")
public class VendorManager extends BaseUserEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @Enumerated(value = EnumType.STRING)
    private VendorRole role;

    private Boolean permission;

    protected VendorManager() {/* empty */}

    private VendorManager(Builder builder) {
        super(builder.email, builder.name, builder.password, builder.phoneNumber);
        this.id = builder.id;
        this.vendor = builder.vendor;
        this.role = builder.role;
        this.permission = builder.permission;
    }

    public Long getId() {
        return id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public VendorRole getRole() {
        return role;
    }

    public Boolean getPermission() {
        return permission;
    }

    //== business ==//
    public boolean isAdmin() {
        return this.role == VendorRole.VENDOR_ROOT;
    }

    public boolean login(String plainTextPassword) {
        if (super.login(plainTextPassword)) {
            if (permission) return true;
            else throw new LoginException(Error.NOT_APPROVED);
        } else {
            throw new LoginException(Error.LOGIN_FAIL, ErrorDetail.INCORRECT_PWD, true);
        }
    }

    public Map<String, Set<String>> approve(List<VendorManager> targets) {
        if (!isAdmin()) {
            throw new NoPermissionException(Error.NO_PERMISSION, ErrorDetail.NOT_ADMIN);
        }

        Set<String> success = new HashSet<>();
        Set<String> fail = new HashSet<>();
        for (VendorManager target : targets) {
            if (!target.permission) {
                target.permission = true;
                success.add(target.getEmail());
            } else fail.add(target.getEmail());
        }

        Map<String, Set<String>> map = new HashMap<>();
        map.put("success", success);
        map.put("fail", fail);

        return map;
    }

    public boolean isApproved() {
        return this.permission;
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private final String email;
        private final String name;
        private final String password;
        private final String phoneNumber;
        private final Vendor vendor;
        private VendorRole role = VendorRole.VENDOR_MANAGER;
        private Boolean permission = false;

        public Builder(String email, String name, String password, String phoneNumber, Vendor vendor) {
            this.email = email;
            this.name = name;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.vendor = vendor;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder role(VendorRole role) {
            this.role = role;
            return this;
        }

        public Builder permission(Boolean permission) {
            this.permission = permission;
            return this;
        }

        public VendorManager build() {
            return new VendorManager(this);
        }
    }
}
