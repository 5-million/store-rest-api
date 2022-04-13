package xyz.fm.storerestapi.dto.category;

import xyz.fm.storerestapi.entity.category.Category;

public class CategoryRegisterResponse extends CategoryInfo {

    private CategoryInfo parent;

    public CategoryRegisterResponse(Category category) {
        super(category.getId(), category.getCategoryName(), category.getDepth());
        this.parent = new CategoryInfo(category.getParent());
    }

    public CategoryRegisterResponse(Long categoryId, String categoryName, Integer depth, CategoryInfo parent) {
        super(categoryId, categoryName, depth);
        this.parent = parent;
    }

    public CategoryInfo getParent() {
        return parent;
    }
}
