package xyz.fm.storerestapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;
    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<Category> child = new ArrayList<>();

    //== builder customize ==//
    public static class CategoryBuilder {

        public CategoryBuilder parent(Category parent) {
            if (parent != null) {
                this.parent = parent;
                this.depth = parent.depth + 1;
            }

            return this;
        }
    }
}
