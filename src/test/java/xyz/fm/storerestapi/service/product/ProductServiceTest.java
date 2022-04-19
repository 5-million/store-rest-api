package xyz.fm.storerestapi.service.product;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.fm.storerestapi.repository.product.ItemRepository;
import xyz.fm.storerestapi.repository.product.ProductRepository;
import xyz.fm.storerestapi.repository.product.VendorItemRepository;
import xyz.fm.storerestapi.service.category.CategoryService;
import xyz.fm.storerestapi.service.user.vendor.VendorService;

@ExtendWith(MockitoExtension.class)
abstract class ProductServiceTest {

    @Mock protected ProductRepository productRepository;
    @Mock protected ItemRepository itemRepository;
    @Mock protected VendorItemRepository vendorItemRepository;
    @Mock protected VendorService vendorService;
    @Mock protected CategoryService categoryService;
    @InjectMocks protected ProductService productService;

}