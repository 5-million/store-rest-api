package xyz.fm.storerestapi.entity.item;

import xyz.fm.storerestapi.entity.BaseEntity;
import xyz.fm.storerestapi.entity.product.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STORE_ITEM")
public class Item extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String selections;
    private Integer salesQuantity;

    @OneToMany(mappedBy = "item")
    private List<VendorItem> vendorItemList = new ArrayList<>();

    protected Item() {/* empty */}

    private Item(Builder builder) {
        this.id = builder.id;
        this.product = builder.product;
        this.selections = builder.selections;
        this.salesQuantity = builder.salesQuantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getSelections() {
        return selections;
    }

    public Integer getSalesQuantity() {
        return salesQuantity;
    }

    public List<VendorItem> getVendorItemList() {
        return vendorItemList;
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private final Product product;
        private String selections;
        private Integer salesQuantity = 0;

        public Builder(Product product) {
            this.product = product;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder selections(String selections) {
            this.selections = selections;
            return this;
        }

        public Builder salesQuantity(Integer salesQuantity) {
            this.salesQuantity = salesQuantity;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }

}
