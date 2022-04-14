package xyz.fm.storerestapi.dto.category;

import xyz.fm.storerestapi.entity.category.Category;

public class CategoryInfoWithParent extends CategoryInfo {

    private CategoryInfo parent;

    public CategoryInfoWithParent(Category category) {
        super(category.getId(), category.getCategoryName(), category.getDepth());
        this.parent = category.getParent() != null ? new CategoryInfo(category.getParent()) : null;
    }

    public CategoryInfoWithParent(Long categoryId, String categoryName, Integer depth, CategoryInfo parent) {
        super(categoryId, categoryName, depth);
        this.parent = parent;
    }

    public CategoryInfo getParent() {
        return parent;
    }
}
