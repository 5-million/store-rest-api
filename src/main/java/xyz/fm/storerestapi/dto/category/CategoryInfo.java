package xyz.fm.storerestapi.dto.category;

import xyz.fm.storerestapi.entity.category.Category;

public class CategoryInfo {

    private Long categoryId;
    private String categoryName;
    private Integer depth;

    public CategoryInfo(Category category) {
        this.categoryId = category.getId();
        this.categoryName = category.getCategoryName();
        this.depth = category.getDepth();
    }

    public CategoryInfo(Long categoryId, String categoryName, Integer depth) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.depth = depth;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getDepth() {
        return depth;
    }
}
