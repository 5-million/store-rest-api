package xyz.fm.storerestapi.dto.product;

public class SimpleProduct {

    private Long categoryId;
    private String categoryName;

    private Long productId;
    private String productName;

    private Long itemId;
    private String selections;
    private Integer salesQuantity;

    private Long vendorId;

    private Long vendorItemId;
    private Integer price;
    private Integer discountRate;
    private Integer discountPrice;
    private Integer rank;

    public SimpleProduct(Long categoryId, String categoryName, Long productId, String productName, Long itemId, String selections, Integer salesQuantity, Long vendorId, Long vendorItemId, Integer price, Integer discountRate) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productId = productId;
        this.productName = productName;
        this.itemId = itemId;
        this.selections = selections;
        this.salesQuantity = salesQuantity;
        this.vendorId = vendorId;
        this.vendorItemId = vendorItemId;
        this.price = price;
        this.discountRate = discountRate;
        this.discountPrice = (int) (price * (1 - (discountRate / 100.0)));
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getSelections() {
        return selections;
    }

    public Integer getSalesQuantity() {
        return salesQuantity;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public Long getVendorItemId() {
        return vendorItemId;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SimpleProduct(");
        sb.append("rank: " + rank);
        sb.append(", categoryId: " + categoryId);
        sb.append(", categoryName: " + categoryName);
        sb.append(", productId: " + productId);
        sb.append(", productName: " + productName);
        sb.append(", itemId: " + itemId);
        sb.append(", selections: " + selections);
        sb.append(", salesQuantity: " + salesQuantity);
        sb.append(", vendorId: " + vendorId);
        sb.append(", vendorItemId: " + vendorItemId);
        sb.append(", price: " + price);
        sb.append(", discountRate: " + discountRate);
        sb.append(", discountPrice: " + discountPrice);
        return sb.toString();
    }
}
