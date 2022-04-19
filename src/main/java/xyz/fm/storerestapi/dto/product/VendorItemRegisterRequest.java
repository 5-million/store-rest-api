package xyz.fm.storerestapi.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VendorItemRegisterRequest {

    @JsonIgnore
    private String managerEmail;

    private Long itemId;
    private Integer price;
    private Integer discountRate;
    private Integer stock;

    public VendorItemRegisterRequest() {/* empty */}

    public VendorItemRegisterRequest(Long itemId, Integer price, Integer discountRate, Integer stock) {
        this.itemId = itemId;
        this.price = price;
        this.discountRate = discountRate;
        this.stock = stock;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public Long getItemId() {
        return itemId;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public Integer getStock() {
        return stock;
    }
}
