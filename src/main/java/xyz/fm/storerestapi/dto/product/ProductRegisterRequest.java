package xyz.fm.storerestapi.dto.product;

public class ProductRegisterRequest {

    private String productName;
    private String options;
    private Long categoryId;
    private ItemRegisterRequest itemRegisterRequest;

    public ProductRegisterRequest() {/* empty */}

    public ProductRegisterRequest(String productName, String options, Long categoryId, ItemRegisterRequest itemRegisterRequest) {
        this.productName = productName;
        this.options = options;
        this.categoryId = categoryId;
        this.itemRegisterRequest = itemRegisterRequest;
    }

    public String getProductName() {
        return productName;
    }

    public String getOptions() {
        return options;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public ItemRegisterRequest getItemRegisterRequest() {
        return itemRegisterRequest;
    }
}
