package xyz.fm.storerestapi.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.fm.storerestapi.entity.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@ToString
public class CategoryInherit {

    private Long categoryId;
    private String categoryName;
    private int depth;
    private List<CategoryInherit> child;

    public CategoryInherit(Category category) {
        this.categoryId = category.getId();
        this.categoryName = category.getName();
        this.depth = category.getDepth();
        this.child = category.getChild().stream().map(CategoryInherit::new).collect(Collectors.toList());
    }
}
