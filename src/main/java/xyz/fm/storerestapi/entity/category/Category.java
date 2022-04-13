package xyz.fm.storerestapi.entity.category;

import xyz.fm.storerestapi.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STORE_CATEGORY")
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private String categoryName;
    private Integer depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private final List<Category> child = new ArrayList<>();

    protected Category() {/* empty */}

    private Category(Builder builder) {
        this.id = builder.id;
        this.categoryName = builder.categoryName;
        this.depth = builder.depth;
        this.parent = builder.parent;
    }

    public Long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getDepth() {
        return depth;
    }

    public Category getParent() {
        return parent;
    }

    public List<Category> getChild() {
        return child;
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private final String categoryName;
        private Integer depth;
        private Category parent;

        public Builder(String categoryName) {
            this.categoryName = categoryName;
            this.depth = 0;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder parent(Category parent) {
            this.parent = parent;
            this.depth = parent.depth + 1;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}
