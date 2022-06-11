package xyz.fm.storerestapi.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.fm.storerestapi.entity.Category;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBriefInfo {

    private Long categoryId;
    private String categoryName;
    private Integer depth;
    private Long parentId;

    public static CategoryBriefInfo of(Category category) {
        return new CategoryBriefInfo(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getParent() == null ? null : category.getParent().getId()
        );
    }
}
