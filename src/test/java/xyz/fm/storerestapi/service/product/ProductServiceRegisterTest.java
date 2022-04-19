package xyz.fm.storerestapi.service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.product.ItemRegisterRequest;
import xyz.fm.storerestapi.dto.product.ProductRegisterRequest;
import xyz.fm.storerestapi.dto.product.VendorItemRegisterRequest;
import xyz.fm.storerestapi.entity.category.Category;
import xyz.fm.storerestapi.entity.item.Item;
import xyz.fm.storerestapi.entity.item.VendorItem;
import xyz.fm.storerestapi.entity.product.Product;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.entity.user.vendor.VendorRole;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NoPermissionException;
import xyz.fm.storerestapi.error.exception.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProductServiceRegisterTest extends ProductServiceTest {

    @Test
    @DisplayName("registerProduct fail: not found category")
    public void registerProduct_fail_notFoundCategory() throws Exception {
        //given
        ProductRegisterRequest request = new ProductRegisterRequest("ipad", null, 1L, null);
        given(categoryService.getById(anyLong()))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_CATEGORY));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.registerProduct(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_CATEGORY);
    }

    @Test
    @DisplayName("registerProduct success")
    public void registerProduct_success() throws Exception {
        //given
        Category category = new Category.Builder("tablet").id(1L).build();

        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").build();
        VendorManager manager = new VendorManager.Builder("manager@vendor.com", "manager", "pwd", "01012346578", vendor)
                .permission(true).build();

        VendorItemRegisterRequest vendorItemRegisterRequest = new VendorItemRegisterRequest(null, 10000, 10, 100);
        vendorItemRegisterRequest.setManagerEmail(manager.getEmail());

        ItemRegisterRequest itemRegisterRequest = new ItemRegisterRequest(null, null, vendorItemRegisterRequest);

        ProductRegisterRequest request = new ProductRegisterRequest("ipad", null, category.getId(), itemRegisterRequest);

        given(categoryService.getById(anyLong())).willReturn(category);
        given(vendorService.getManagerByEmail(anyString())).willReturn(manager);

        //when
        Product result = productService.registerProduct(request);

        //then
        assertThat(result.getProductName()).isEqualTo(request.getProductName());
        assertThat(result.getCategory()).isEqualTo(category);
        assertThat(result.getItems().size()).isEqualTo(1);
        assertThat(vendor.getItems().size()).isEqualTo(1);

        verify(productRepository, times(1)).save(any(Product.class));
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(vendorItemRepository, times(1)).save(any(VendorItem.class));
    }

    @Test
    @DisplayName("registerItem fail: not found product")
    public void registerItem_fail_notFoundProduct() throws Exception {
        //given
        ItemRegisterRequest request = new ItemRegisterRequest(1L, null, null);
        given(productRepository.findById(anyLong()))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_PRODUCT));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.registerItem(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_PRODUCT);
    }

    @Test
    @DisplayName("registerItem success")
    public void registerItem_success() throws Exception {
        //given
        Category category = new Category.Builder("tablet").id(1L).build();
        Product product = new Product.Builder("ipad", category).id(2L).build();

        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").build();
        VendorManager manager = new VendorManager.Builder("manager@vendor.com", "manager", "pwd", "01012346578", vendor)
                .permission(true).build();

        VendorItemRegisterRequest vendorItemRegisterRequest = new VendorItemRegisterRequest(null, 10000, 10, 100);
        vendorItemRegisterRequest.setManagerEmail(manager.getEmail());

        ItemRegisterRequest request = new ItemRegisterRequest(product.getId(), null, vendorItemRegisterRequest);

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(vendorService.getManagerByEmail(anyString())).willReturn(manager);

        //when
        Item result = productService.registerItem(request);

        //then
        assertThat(result.getProduct().getId()).isEqualTo(product.getId());
        assertThat(result.getVendorItemList().size()).isEqualTo(1);
        assertThat(result.getVendorItemList().get(0).getPrice()).isEqualTo(vendorItemRegisterRequest.getPrice());
    }

    @Test
    @DisplayName("registerVendorItem fail: not found item")
    public void registerVendorItem_fail_notFoundItem() throws Exception {
        //given
        VendorItemRegisterRequest request = new VendorItemRegisterRequest(1L, 10000, 10, 100);
        given(itemRepository.findById(anyLong()))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_ITEM));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.registerVendorItem(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_ITEM);
    }

    @Test
    @DisplayName("registerVendorItem fail: not found vendor")
    public void registerVendorItem_fail_notFoundVendor() throws Exception {
        //given
        VendorItemRegisterRequest request = new VendorItemRegisterRequest(1L, 10000, 10, 100);
        request.setManagerEmail("manager@vendor.com");

        given(vendorService.getManagerByEmail(anyString()))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_USER));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.registerVendorItem(null, request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_USER);
    }

    @Test
    @DisplayName("registerVendorItem fail: no permission")
    public void registerVendorItem_fail_noPermission() throws Exception {
        //given
        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").build();
        VendorManager manager = new VendorManager.Builder("manager@apple.com", "name", "pwd", "01012345678", vendor)
                .role(VendorRole.VENDOR_MANAGER)
                .permission(false)
                .build();

        Category category = new Category.Builder("tablet").build();
        Product product = new Product.Builder("ipad", category).build();
        Item item = new Item.Builder(product).build();

        VendorItemRegisterRequest request = new VendorItemRegisterRequest(1L, 10000, 10, 100);
        request.setManagerEmail(manager.getEmail());

        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));
        given(vendorService.getManagerByEmail(anyString())).willReturn(manager);

        //when
        NoPermissionException exception = assertThrows(NoPermissionException.class, () -> productService.registerVendorItem(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NO_PERMISSION);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NO_PERMISSION);
    }

    @Test
    @DisplayName("registerVendorItem success")
    public void registerVendorItem_success() throws Exception {
        //given
        VendorItemRegisterRequest request = new VendorItemRegisterRequest(null, 10000, 10, 100);
        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").build();
        VendorManager manager = new VendorManager.Builder("manager@apple.com", "name", "pwd", "01012345678", vendor)
                .role(VendorRole.VENDOR_MANAGER)
                .permission(true)
                .build();
        request.setManagerEmail(manager.getEmail());

        Category category = new Category.Builder("tablet").build();
        Product product = new Product.Builder("ipad", category).build();
        Item item = new Item.Builder(product).build();

        given(vendorService.getManagerByEmail(anyString())).willReturn(manager);

        //when
        VendorItem result = productService.registerVendorItem(item, request);

        //then
        assertThat(result.getPrice()).isEqualTo(request.getPrice());
        assertThat(result.getDiscountRate()).isEqualTo(request.getDiscountRate());
        assertThat(result.getStock()).isEqualTo(request.getStock());
        assertThat(result.getVendor().getVendorName()).isEqualTo(vendor.getVendorName());
        assertThat(result.getItem()).isEqualTo(item);
        verify(vendorItemRepository, times(1)).save(any(VendorItem.class));
    }
}
