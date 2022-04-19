package xyz.fm.storerestapi.entity.item;

import xyz.fm.storerestapi.entity.BaseEntity;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;

import javax.persistence.*;

@Entity
@Table(name = "STORE_VENDOR_ITEM")
public class VendorItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "vendor_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer price;
    private Integer discountRate; // 단위: %
    private Integer stock;

    protected VendorItem() {/* empty */}

    private VendorItem(Builder builder) {
        this.id = builder.id;
        this.vendor = builder.vendor;
        this.item = builder.item;
        this.price = builder.price;
        this.discountRate = builder.discountRate;
        this.stock = builder.stock;
    }

    public Long getId() {
        return id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public Item getItem() {
        return item;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public Integer getStock() {
        return stock;
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private Vendor vendor;
        private final Item item;
        private final Integer price;
        private Integer discountRate = 0;
        private Integer stock = 0;

        public Builder(Item item, Integer price) {
            this.item = item;
            this.price = price;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder vendor(Vendor vendor) {
            this.vendor = vendor;
            return this;
        }

        public Builder discountRate(Integer discountRate) {
            this.discountRate = discountRate;
            return this;
        }

        public Builder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public VendorItem build() {
            return new VendorItem(this);
        }
    }
}
