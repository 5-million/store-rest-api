package xyz.fm.storerestapi.entity.product;

import xyz.fm.storerestapi.entity.BaseEntity;
import xyz.fm.storerestapi.entity.category.Category;
import xyz.fm.storerestapi.entity.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STORE_PRODUCT")
public class Product extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String productName;
    private String options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private final List<Item> items = new ArrayList<>();

    protected Product() {/* empty */}

    private Product(Builder builder) {
        this.id = builder.id;
        this.productName = builder.productName;
        this.options = builder.options;
        this.category = builder.category;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getOptions() {
        return options;
    }

    public Category getCategory() {
        return category;
    }

    public List<Item> getItems() {
        return items;
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private final String productName;
        private String options;
        private final Category category;

        public Builder(String productName, Category category) {
            this.productName = productName;
            this.category = category;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder options(String options) {
            this.options = options;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
