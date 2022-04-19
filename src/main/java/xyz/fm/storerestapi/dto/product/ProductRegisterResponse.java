package xyz.fm.storerestapi.dto.product;

import xyz.fm.storerestapi.dto.category.CategoryInfo;
import xyz.fm.storerestapi.dto.user.vendor.VendorInfo;
import xyz.fm.storerestapi.entity.item.Item;
import xyz.fm.storerestapi.entity.item.VendorItem;
import xyz.fm.storerestapi.entity.product.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductRegisterResponse {

    private CategoryInfo category;
    private Long productId;
    private String productName;
    private Map<String, String> options = new HashMap<>();
    private Long itemId;
    private Long vendorItemId;
    private Integer price;
    private Integer discountRate;
    private Integer stock;
    private VendorInfo vendor;

    public ProductRegisterResponse(Product product) {
        Item item = product.getItems().get(product.getItems().size() - 1);
        VendorItem vendorItem = item.getVendorItemList().get(item.getVendorItemList().size() - 1);

        this.category = new CategoryInfo(product.getCategory());

        this.productId = product.getId();
        this.productName = product.getProductName();

        for (int i = 0; i < product.getOptions().size(); i++) {
            options.put(product.getOptions().get(i), item.getSelections().get(i));
        }

        this.itemId = item.getId();
        this.vendorItemId = vendorItem.getId();
        this.price = vendorItem.getPrice();
        this.discountRate = vendorItem.getDiscountRate();
        this.stock = vendorItem.getStock();

        this.vendor = VendorInfo.of(vendorItem.getVendor());
    }

    public ProductRegisterResponse(Item item) {
        this(item.getProduct());
    }

    public ProductRegisterResponse(VendorItem vendorItem) {
        this(vendorItem.getItem());
    }

    public CategoryInfo getCategory() {
        return category;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public Long getItemId() {
        return itemId;
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

    public Integer getStock() {
        return stock;
    }

    public VendorInfo getVendor() {
        return vendor;
    }
}
