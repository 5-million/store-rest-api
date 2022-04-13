package xyz.fm.storerestapi.entity.user.vendor;

import xyz.fm.storerestapi.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STORE_VENDOR")
public class Vendor extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "vendor_id")
    private Long id;

    private String vendorName;
    private String ceo;

    @Column(unique = true)
    private String registrationNumber;

    private String location;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<VendorManager> managers = new ArrayList<>();

    protected Vendor() {/* empty */}

    private Vendor(Builder builder) {
        this.id = builder.id;
        this.vendorName = builder.vendorName;
        this.ceo = builder.ceo;
        this.registrationNumber = builder.registrationNumber;
        this.location = builder.location;
    }

    public Long getId() {
        return id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getCeo() {
        return ceo;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getLocation() {
        return location;
    }

    public List<VendorManager> getManagers() {
        return managers;
    }

    //== business ==//
    public void addManager(VendorManager manager) {
        this.managers.add(manager);
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private final String vendorName;
        private final String ceo;
        private final String registrationNumber;
        private final String location;

        public Builder(String vendorName, String ceo, String registrationNumber, String location) {
            this.vendorName = vendorName;
            this.ceo = ceo;
            this.registrationNumber = registrationNumber;
            this.location = location;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Vendor build() {
            return new Vendor(this);
        }
    }
}
