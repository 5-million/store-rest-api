package xyz.fm.storerestapi.entity;

import lombok.*;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Vendor extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "vendor_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String regNumber;

    private String ceo;

    @Embedded
    private Address location;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    @Builder.Default
    private List<VendorManager> vendorManagerList = new ArrayList<>();

    //== business ==//
    public void addManager(VendorManager manager) {
        manager.setVendor(this);
        vendorManagerList.add(manager);
    }

    @Override
    public String toString() {
        return "Vendor(" + "id: " + id +
                ", name: " + name +
                ", regNumber: " + regNumber +
                ", ceo: " + ceo +
                ", " + location +
                ")";
    }
}
