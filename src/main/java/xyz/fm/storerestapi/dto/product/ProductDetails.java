package xyz.fm.storerestapi.dto.product;

import xyz.fm.storerestapi.dto.category.CategoryInfo;
import xyz.fm.storerestapi.dto.user.vendor.VendorInfo;
import xyz.fm.storerestapi.entity.item.Item;
import xyz.fm.storerestapi.entity.item.VendorItem;
import xyz.fm.storerestapi.entity.product.Product;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class ProductDetails {

    private CategoryInfo category;

    private Long productId;
    private Long itemId;
    private String productName;
    private Map<String, String> selectedOption = new HashMap<>();
    private Map<String, Set<String>> options = new HashMap<>();

    private Long vendorItemId;
    private VendorInfo vendor;
    private Integer stock;
    private Integer originalPrice;
    private Integer discountRate;
    private Integer discountPrice;

    private List<VendorInfo> otherVendor;

    public ProductDetails(Product product, Long itemId, Long vendorItemId) {
        Item selectedItem = getItemById(product.getItemsSortBySalesQuantity(), itemId);
        VendorItem selectedVendorItem = getVendorItemById(selectedItem.getVendorItemListSortByPrice(), vendorItemId);

        this.category = new CategoryInfo(product.getCategory());
        this.productId = product.getId();
        this.itemId = selectedItem.getId();
        this.productName = product.getProductName();

        for (int i = 0; i < product.getOptions().size(); i++) {
            String optionName = product.getOptions().get(i);
            selectedOption.put(optionName, selectedItem.getSelections().get(i));

            final int index = i;
            options.put(optionName, product.getItemsSortBySalesQuantity().stream().map(item -> item.getSelections().get(index)).collect(Collectors.toSet()));
        }

        this.vendorItemId = selectedVendorItem.getId();
        this.vendor = VendorInfo.of(selectedVendorItem.getVendor());
        this.stock = selectedVendorItem.getStock();
        this.originalPrice = selectedVendorItem.getPrice();
        this.discountRate = selectedVendorItem.getDiscountRate();
        this.discountPrice = selectedVendorItem.realPrice();

        this.otherVendor = selectedItem.getVendorItemListSortByPrice()
                .stream()
                .filter(vi -> !Objects.equals(vi.getId(), selectedVendorItem.getId()))
                .map(vi -> VendorInfo.of(vi.getVendor()))
                .collect(Collectors.toList());
    }

    private Item getItemById(List<Item> items, Long itemId) {
        if (items.isEmpty()) throw new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_ITEM);
        for (Item item : items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }

        return items.get(0);
    }

    private VendorItem getVendorItemById(List<VendorItem> vendorItems, Long vendorItemId) {
        if (vendorItems.isEmpty()) throw new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_ITEM);
        for (VendorItem vendorItem : vendorItems) {
            if (vendorItem.getId().equals(vendorItemId)) {
                return vendorItem;
            }
        }

        return vendorItems.get(0);
    }

    public CategoryInfo getCategory() {
        return category;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getProductName() {
        return productName;
    }

    public Map<String, String> getSelectedOption() {
        return selectedOption;
    }

    public Map<String, Set<String>> getOptions() {
        return options;
    }

    public Long getVendorItemId() {
        return vendorItemId;
    }

    public VendorInfo getVendor() {
        return vendor;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public List<VendorInfo> getOtherVendor() {
        return otherVendor;
    }
}
