package xyz.fm.storerestapi.dto.product;

public class ItemRegisterRequest {

    private Long productId;
    private String selections;
    private VendorItemRegisterRequest vendorItemRegisterRequest;

    public ItemRegisterRequest() {/* empty */}

    public ItemRegisterRequest(Long productId, String selections, VendorItemRegisterRequest vendorItemRegisterRequest) {
        this.productId = productId;
        this.selections = selections;
        this.vendorItemRegisterRequest = vendorItemRegisterRequest;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getSelections() {
        return selections;
    }

    public VendorItemRegisterRequest getVendorItemRegisterRequest() {
        return vendorItemRegisterRequest;
    }
}
