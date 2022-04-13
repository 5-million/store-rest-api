package xyz.fm.storerestapi.dto.category;

public class CategoryRegisterRequest {

    private Long parentId;
    private String categoryName;

    public CategoryRegisterRequest() {/* empty */}

    public CategoryRegisterRequest(Long parentId, String categoryName) {
        this.parentId = parentId;
        this.categoryName = categoryName;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
