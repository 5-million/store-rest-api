package xyz.fm.storerestapi.service.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.dto.product.ItemRegisterRequest;
import xyz.fm.storerestapi.dto.product.ProductRegisterRequest;
import xyz.fm.storerestapi.dto.product.VendorItemRegisterRequest;
import xyz.fm.storerestapi.entity.category.Category;
import xyz.fm.storerestapi.entity.item.Item;
import xyz.fm.storerestapi.entity.item.VendorItem;
import xyz.fm.storerestapi.entity.product.Product;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NoPermissionException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.repository.product.ItemRepository;
import xyz.fm.storerestapi.repository.product.ProductRepository;
import xyz.fm.storerestapi.repository.product.VendorItemRepository;
import xyz.fm.storerestapi.service.category.CategoryService;
import xyz.fm.storerestapi.service.user.vendor.VendorService;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final VendorItemRepository vendorItemRepository;
    private final VendorService vendorService;
    private final CategoryService categoryService;

    public ProductService(
            ProductRepository productRepository,
            ItemRepository itemRepository,
            VendorItemRepository vendorItemRepository,
            VendorService vendorService,
            CategoryService categoryService) {
        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
        this.vendorItemRepository = vendorItemRepository;
        this.vendorService = vendorService;
        this.categoryService = categoryService;
    }

    public Product registerProduct(ProductRegisterRequest request) {
        Category category = categoryService.getById(request.getCategoryId());
        Product product = new Product.Builder(request.getProductName(), category).build();
        productRepository.save(product);
        product.addItem(registerItem(product, request.getItemRegisterRequest()));

        return product;
    }

    public Item registerItem(ItemRegisterRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_PRODUCT));

        return registerItem(product, request);
    }

    public Item registerItem(Product product, ItemRegisterRequest request) {
        Item item = new Item.Builder(product).selections(request.getSelections()).build();
        itemRepository.save(item);
        item.addVendorItem(registerVendorItem(item, request.getVendorItemRegisterRequest()));

        return item;
    }

    public VendorItem registerVendorItem(VendorItemRegisterRequest request) {
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_ITEM));

        return registerVendorItem(item, request);
    }

    public VendorItem registerVendorItem(Item item, VendorItemRegisterRequest request) {
        VendorManager manager = vendorService.getManagerByEmail(request.getManagerEmail());
        if (!manager.isApproved()) {
            throw new NoPermissionException(Error.NO_PERMISSION, ErrorDetail.NO_PERMISSION);
        }

        Vendor vendor = manager.getVendor();
        VendorItem vendorItem = new VendorItem.Builder(item, request.getPrice()).vendor(vendor).build();
        vendorItemRepository.save(vendorItem);
        vendor.addItem(vendorItem);

        return vendorItem;
    }
}
