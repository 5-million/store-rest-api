package xyz.fm.storerestapi.controller.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.product.ItemRegisterRequest;
import xyz.fm.storerestapi.dto.product.ProductRegisterRequest;
import xyz.fm.storerestapi.dto.product.VendorItemRegisterRequest;
import xyz.fm.storerestapi.entity.category.Category;
import xyz.fm.storerestapi.entity.item.Item;
import xyz.fm.storerestapi.entity.item.VendorItem;
import xyz.fm.storerestapi.entity.product.Product;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NoPermissionException;
import xyz.fm.storerestapi.error.exception.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_KEY;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_VALUE_PREFIX;

public class ProductRestControllerRegisterTest extends ProductRestControllerTest {

    private final String productUrl = BASE_URL;
    private final String itemUrl = BASE_URL + "/item";
    private final String vendorItemUrl = itemUrl + "/vendorItem";
    private final String managerEmail = "manager@vendor.com";

    private final VendorItemRegisterRequest vendorItemRegisterRequestFrame = new VendorItemRegisterRequest();
    private final ItemRegisterRequest itemRegisterRequestFrame = new ItemRegisterRequest(null, null, vendorItemRegisterRequestFrame);
    private final ProductRegisterRequest productRegisterRequestFrame = new ProductRegisterRequest(null, null, null, itemRegisterRequestFrame);
    
    @Test
    @DisplayName("registerProduct fail: token not included")
    public void registerProduct_fail_tokenNotIncluded() throws Exception {
        //given

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(productUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(productRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));
    }

    @Test
    @DisplayName("registerProduct fail: not found exception")
    public void registerProduct_fail_notFoundException() throws Exception {
        //given
        given(productService.registerProduct(any(ProductRegisterRequest.class)))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_USER));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(productUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + managerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(productRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("registerProduct"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("registerProduct fail: no permission")
    public void registerProduct_fail_noPermission() throws Exception {
        //given
        given(productService.registerProduct(any(ProductRegisterRequest.class)))
                .willThrow(new NoPermissionException(Error.NO_PERMISSION, ErrorDetail.NO_PERMISSION));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(productUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + managerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(productRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("registerProduct"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value(Error.NO_PERMISSION.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NO_PERMISSION));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NoPermissionException.class);
    }

    @Test
    @DisplayName("registerProduct success")
    public void registerProduct_success() throws Exception {
        //given
        Category category = new Category.Builder("tablet").id(1L).build();
        Product product = new Product.Builder("ipad", category).id(2L).options("color").build();
        Item item = new Item.Builder(product).id(3L).selections("black").build();
        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").id(4L).build();
        VendorItem vendorItem = new VendorItem.Builder(item, 10000).discountRate(10).stock(1000).vendor(vendor).id(5L).build();

        item.addVendorItem(vendorItem);
        product.addItem(item);
        vendor.addItem(vendorItem);

        given(productService.registerProduct(any(ProductRegisterRequest.class))).willReturn(product);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(productUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + managerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(productRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("registerProduct"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category.categoryId").value(category.getId()))
                .andExpect(jsonPath("$.category.categoryName").value(category.getCategoryName()))
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.options").isMap())
                .andExpect(jsonPath("$.itemId").value(item.getId()))
                .andExpect(jsonPath("$.vendorItemId").value(vendorItem.getId()))
                .andExpect(jsonPath("$.price").value(vendorItem.getPrice()))
                .andExpect(jsonPath("$.discountRate").value(vendorItem.getDiscountRate()))
                .andExpect(jsonPath("$.stock").value(vendorItem.getStock()))
                .andExpect(jsonPath("$.vendor.vendorId").value(vendor.getId()))
                .andExpect(jsonPath("$.vendor.vendorName").value(vendor.getVendorName()))
                .andExpect(jsonPath("$.vendor.ceo").value(vendor.getCeo()))
                .andExpect(jsonPath("$.vendor.registrationNumber").value(vendor.getRegistrationNumber()))
                .andExpect(jsonPath("$.vendor.location").value(vendor.getLocation()));
    }

    @Test
    @DisplayName("registerItem fail: token not included")
    public void registerItem_fail_tokenNotIncluded() throws Exception {
        //given

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(itemUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(itemRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));
    }

    @Test
    @DisplayName("registerItem fail: not found")
    public void registerItem_fail_notFound() throws Exception {
        //given
        given(productService.registerItem(any(ItemRegisterRequest.class)))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_USER));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(itemUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + managerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(itemRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("registerItem"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("registerItem fail: no permission")
    public void registerItem_fail_noPermission() throws Exception {
        //given
        given(productService.registerItem(any(ItemRegisterRequest.class)))
                .willThrow(new NoPermissionException(Error.NO_PERMISSION, ErrorDetail.NO_PERMISSION));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(itemUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + managerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(itemRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("registerItem"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value(Error.NO_PERMISSION.getCode()));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NoPermissionException.class);
    }

    @Test
    @DisplayName("registerItem success")
    public void registerItem_success() throws Exception {
        //given
        Category category = new Category.Builder("tablet").id(1L).build();
        Product product = new Product.Builder("ipad", category).id(2L).options("color,storage").build();
        Item item = new Item.Builder(product).id(3L).selections("black,256GB").build();
        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").id(4L).build();
        VendorItem vendorItem = new VendorItem.Builder(item, 10000).discountRate(10).stock(1000).vendor(vendor).id(5L).build();

        item.addVendorItem(vendorItem);
        product.addItem(item);
        vendor.addItem(vendorItem);

        given(productService.registerItem(any(ItemRegisterRequest.class))).willReturn(item);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(itemUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + managerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(itemRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("registerItem"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category.categoryId").value(category.getId()))
                .andExpect(jsonPath("$.category.categoryName").value(category.getCategoryName()))
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.options").isMap())
                .andExpect(jsonPath("$.itemId").value(item.getId()))
                .andExpect(jsonPath("$.vendorItemId").value(vendorItem.getId()))
                .andExpect(jsonPath("$.price").value(vendorItem.getPrice()))
                .andExpect(jsonPath("$.discountRate").value(vendorItem.getDiscountRate()))
                .andExpect(jsonPath("$.stock").value(vendorItem.getStock()))
                .andExpect(jsonPath("$.vendor.vendorId").value(vendor.getId()))
                .andExpect(jsonPath("$.vendor.vendorName").value(vendor.getVendorName()))
                .andExpect(jsonPath("$.vendor.ceo").value(vendor.getCeo()))
                .andExpect(jsonPath("$.vendor.registrationNumber").value(vendor.getRegistrationNumber()))
                .andExpect(jsonPath("$.vendor.location").value(vendor.getLocation()));
    }

    @Test
    @DisplayName("registerVendorItem fail: token not included")
    public void registerVendorItem_fail_tokeNotIncluded() throws Exception {
        //given

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(vendorItemUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(vendorItemRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));
    }

    @Test
    @DisplayName("registerVendorItem fail: not found")
    public void registerVendorItem_fail_notFound() throws Exception {
        //given
        given(productService.registerVendorItem(any(VendorItemRegisterRequest.class)))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_USER));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(vendorItemUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + managerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(vendorItemRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("registerVendorItem"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("registerVendorItem fail: no permission")
    public void registerVendorItem_fail_noPermission() throws Exception {
        //given
        given(productService.registerVendorItem(any(VendorItemRegisterRequest.class)))
                .willThrow(new NoPermissionException(Error.NO_PERMISSION, ErrorDetail.NO_PERMISSION));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(vendorItemUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + managerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(vendorItemRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("registerVendorItem"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value(Error.NO_PERMISSION.getCode()));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NoPermissionException.class);
    }

    @Test
    @DisplayName("registerVendorItem success")
    public void registerVendorItem_success() throws Exception {
        //given
        Category category = new Category.Builder("tablet").id(1L).build();
        Product product = new Product.Builder("ipad", category).id(2L).options("color").build();
        Item item = new Item.Builder(product).id(3L).selections("black").build();
        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").id(4L).build();
        VendorItem vendorItem = new VendorItem.Builder(item, 10000).discountRate(10).stock(1000).vendor(vendor).id(5L).build();

        item.addVendorItem(vendorItem);
        product.addItem(item);
        vendor.addItem(vendorItem);

        given(productService.registerVendorItem(any(VendorItemRegisterRequest.class))).willReturn(vendorItem);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(vendorItemUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + managerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(vendorItemRegisterRequestFrame))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("registerVendorItem"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category.categoryId").value(category.getId()))
                .andExpect(jsonPath("$.category.categoryName").value(category.getCategoryName()))
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.options").isMap())
                .andExpect(jsonPath("$.itemId").value(item.getId()))
                .andExpect(jsonPath("$.vendorItemId").value(vendorItem.getId()))
                .andExpect(jsonPath("$.price").value(vendorItem.getPrice()))
                .andExpect(jsonPath("$.discountRate").value(vendorItem.getDiscountRate()))
                .andExpect(jsonPath("$.stock").value(vendorItem.getStock()))
                .andExpect(jsonPath("$.vendor.vendorId").value(vendor.getId()))
                .andExpect(jsonPath("$.vendor.vendorName").value(vendor.getVendorName()))
                .andExpect(jsonPath("$.vendor.ceo").value(vendor.getCeo()))
                .andExpect(jsonPath("$.vendor.registrationNumber").value(vendor.getRegistrationNumber()))
                .andExpect(jsonPath("$.vendor.location").value(vendor.getLocation()));
    }
}
