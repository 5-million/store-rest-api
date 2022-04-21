package xyz.fm.storerestapi.controller.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.product.SimpleProduct;
import xyz.fm.storerestapi.entity.category.Category;
import xyz.fm.storerestapi.entity.item.Item;
import xyz.fm.storerestapi.entity.item.VendorItem;
import xyz.fm.storerestapi.entity.product.Product;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductRestControllerGetTest extends ProductRestControllerTest {

    @Test
    @DisplayName("getByCategory success")
    public void getByCategory_success() throws Exception {
        //given
        List<SimpleProduct> products = new ArrayList<>();
        products.add(new SimpleProduct(2L, "태블릿PC", 5L, "2021 iPad 9th 10.2inch", 12L, "스페이스 그레이,64GB X Wi-Fi", 777, 4L, 14L, 449000, 4));
        products.add(new SimpleProduct(2L, "태블릿PC", 5L, "2021 iPad 9th 10.2inch", 15L, "실버,64GB X Wi-Fi+Cellular", 452, 4L, 16L, 619000, 0));
        products.add(new SimpleProduct(2L, "태블릿PC", 5L, "2021 iPad 9th 10.2inch", 9L, "실버,64GB X Wi-Fi", 100, 4L, 11L, 449000, 4));
        products.add(new SimpleProduct(2L, "태블릿PC", 5L, "2021 iPad 9th 10.2inch", 6L, "실버,256GB X Wi-Fi", 97, 4L, 8L, 639000, 3));
        products.add(new SimpleProduct(2L, "태블릿PC", 5L, "2021 iPad 9th 10.2inch", 6L, "실버,256GB X Wi-Fi", 97, 3L, 7L, 639000, 0));
        products.add(new SimpleProduct(2L, "태블릿PC", 5L, "2021 iPad 9th 10.2inch", 9L, "실버,64GB X Wi-Fi", 100, 3L, 10L, 449000, 0));
        products.add(new SimpleProduct(2L, "태블릿PC", 5L, "2021 iPad 9th 10.2inch", 12L, "스페이스 그레이,64GB X Wi-Fi", 777, 3L, 13L, 449000, 0));
        products.add(new SimpleProduct(2L, "태블릿PC", 5L, "2021 iPad 9th 10.2inch", 15L, "실버,64GB X Wi-Fi+Cellular", 452, 3L, 17L, 619000, 0));

        given(productApiRepository.findByCategory(anyLong())).willReturn(products);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL)
                        .param("categoryId", "2")
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("getByCategory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray());

        for (int i = 0; i < 4; i++) {
            ra
                    .andExpect(jsonPath("$.products[" + i + "].rank").value(i + 1))
                    .andExpect(jsonPath("$.products[" + i + "].itemId").value(products.get(i).getItemId()))
                    .andExpect(jsonPath("$.products[" + i + "].selections").value(products.get(i).getSelections()))
                    .andExpect(jsonPath("$.products[" + i + "].salesQuantity").value(products.get(i).getSalesQuantity()))
                    .andExpect(jsonPath("$.products[" + i + "].vendorId").value(products.get(i).getVendorId()))
                    .andExpect(jsonPath("$.products[" + i + "].vendorItemId").value(products.get(i).getVendorItemId()))
                    .andExpect(jsonPath("$.products[" + i + "].price").value(products.get(i).getPrice()))
                    .andExpect(jsonPath("$.products[" + i + "].discountRate").value(products.get(i).getDiscountRate()))
                    .andExpect(jsonPath("$.products[" + i + "].discountPrice").value(products.get(i).getDiscountPrice()));
        }
    }

    @Test
    @DisplayName("getDetails fail: not found product")
    public void getDetail_fail_notFoundProduct() throws Exception {
        //given
        given(productService.getAllFetchedById(anyLong()))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_PRODUCT));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/1")
                        .param("itemId", "2")
                        .param("vendorItemId", "3")
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("getDetails"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_PRODUCT));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("getDetails fail: empty item")
    public void getDetails_fail_emptyItem() throws Exception {
        //given
        Category tabletPC = new Category.Builder("태블릿PC").id(2L).build();
        Product iPad9th = new Product.Builder("2021 iPad 9th 10.2inch", tabletPC).options("색상,저장용량 X 태블릿 연결성").id(5L).build();

        given(productService.getAllFetchedById(anyLong())).willReturn(iPad9th);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/5")
                        .param("itemId", "6")
                        .param("vendorItemId", "7")
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("getDetails"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_ITEM));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("getDetails fail: empty vendorItem")
    public void getDetails_fail_emptyVendorItem() throws Exception {
        //given
        Category tabletPC = new Category.Builder("태블릿PC").id(2L).build();
        Product iPad9th = new Product.Builder("2021 iPad 9th 10.2inch", tabletPC).options("색상,저장용량 X 태블릿 연결성").id(5L).build();
        iPad9th.addItem(new Item.Builder(iPad9th).selections("실버,64GB X Wi-Fi").id(6L).build());

        given(productService.getAllFetchedById(anyLong())).willReturn(iPad9th);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/5")
                        .param("itemId", "6")
                        .param("vendorItemId", "7")
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("getDetails"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_ITEM));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("getDetails success: not found itemId: select other item")
    public void getDetails_success_notFoundItemId_selectOtherItem() throws Exception {
        //given
        Product product = buildTestProduct();
        given(productService.getAllFetchedById(anyLong())).willReturn(product);
        long notItemId = 1;
        long notVendorItemId = 2;

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/5")
                        .param("itemId", "" + notItemId)
                        .param("vendorItemId", "" + notVendorItemId)
        ).andDo(print());

        //then
        Item selectedItem = product.getItemsSortBySalesQuantity().get(0);
        VendorItem selectedVendorItem = selectedItem.getVendorItemListSortByPrice().get(0);
        validateGetDetailJson(ra, product, selectedItem, selectedVendorItem);
    }

    private void validateGetDetailJson(ResultActions ra, Product product, Item item, VendorItem vendorItem) throws Exception {
        ra
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("getDetails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category.categoryId").value(product.getCategory().getId()))
                .andExpect(jsonPath("$.category.categoryName").value(product.getCategory().getCategoryName()))
                .andExpect(jsonPath("$.category.depth").value(product.getCategory().getDepth()))
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.itemId").value(item.getId()))
                .andExpect(jsonPath("$.selectedOption").isMap())
                .andExpect(jsonPath("$.options").isMap())
                .andExpect(jsonPath("$.vendorItemId").value(vendorItem.getId()))
                .andExpect(jsonPath("$.vendor.vendorId").value(vendorItem.getVendor().getId()))
                .andExpect(jsonPath("$.vendor.vendorName").value(vendorItem.getVendor().getVendorName()))
                .andExpect(jsonPath("$.vendor.ceo").value(vendorItem.getVendor().getCeo()))
                .andExpect(jsonPath("$.vendor.registrationNumber").value(vendorItem.getVendor().getRegistrationNumber()))
                .andExpect(jsonPath("$.vendor.location").value(vendorItem.getVendor().getLocation()))
                .andExpect(jsonPath("$.stock").value(vendorItem.getStock()))
                .andExpect(jsonPath("$.originalPrice").value(vendorItem.getPrice()))
                .andExpect(jsonPath("$.discountRate").value(vendorItem.getDiscountRate()))
                .andExpect(jsonPath("$.otherVendor").isArray());
    }

    @Test
    @DisplayName("getDetails success: not found vendorItemId: select other vendorItem")
    public void getDetails_success_notFoundItemId_selectOtherVendorItem() throws Exception {
        //given
        Product product = buildTestProduct();
        given(productService.getAllFetchedById(anyLong())).willReturn(product);
        long itemId = 9;
        long notVendorItemId = 1;

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/5")
                        .param("itemId", "" + itemId)
                        .param("vendorItemId", "" + notVendorItemId)
        ).andDo(print());

        //then
        Item selectedItem = product.getItemsSortBySalesQuantity().get(1);
        VendorItem selectedVendorItem = selectedItem.getVendorItemListSortByPrice().get(0);
        validateGetDetailJson(ra, product, selectedItem, selectedVendorItem);
    }

    @Test
    @DisplayName("getProductDetail success")
    public void getProductDetail_success() throws Exception {
        //given
        Product product = buildTestProduct();
        given(productService.getAllFetchedById(anyLong())).willReturn(product);
        long itemId = 12;
        long vendorItemId = 14;

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/5")
                        .param("itemId", "" + itemId)
                        .param("vendorItemId", "" + vendorItemId)
        ).andDo(print());

        //then
        Item selectedItem = product.getItemsSortBySalesQuantity().get(0);
        VendorItem selectedVendorItem = selectedItem.getVendorItemListSortByPrice().get(0);
        validateGetDetailJson(ra, product, selectedItem, selectedVendorItem);
    }

    private Product buildTestProduct() {
        Category homeAppliances = new Category.Builder("가전제품").id(1L).build(); // 1
        Category tabletPC = new Category.Builder("태블릿PC").id(2L).parent(homeAppliances).build(); // 2

        Vendor apple = new Vendor.Builder("apple", "cook", "1", "usa").id(3L).build(); // 3
        Vendor coupang = new Vendor.Builder("coupang", "kang", "2", "seoul").id(4L).build(); // 4

        Product iPad9th = new Product.Builder("2021 iPad 9th 10.2inch", tabletPC).options("색상,저장용량 X 태블릿 연결성").id(5L).build(); // 5

        Item iPad9thSilverWiFi64G = new Item.Builder(iPad9th).selections("실버,64GB X Wi-Fi").salesQuantity(100).id(6L).build(); // 6
        iPad9thSilverWiFi64G.addVendorItem(new VendorItem.Builder(iPad9thSilverWiFi64G, 449000).stock(100).vendor(apple).id(7L).build()); // 7
        iPad9thSilverWiFi64G.addVendorItem(new VendorItem.Builder(iPad9thSilverWiFi64G, 449000).discountRate(4).stock(100).vendor(coupang).id(8L).build()); // 8
        iPad9th.addItem(iPad9thSilverWiFi64G);

        Item iPad9thSilverCellularWiFi64G = new Item.Builder(iPad9th).selections("실버,64GB X Wi-Fi+Cellular").salesQuantity(452).id(9L).build(); // 9
        iPad9thSilverCellularWiFi64G.addVendorItem(new VendorItem.Builder(iPad9thSilverCellularWiFi64G, 619000).vendor(apple).id(10L).build()); // 10
        iPad9thSilverCellularWiFi64G.addVendorItem(new VendorItem.Builder(iPad9thSilverCellularWiFi64G, 610000).vendor(coupang).id(11L).build()); // 11
        iPad9th.addItem(iPad9thSilverCellularWiFi64G);

        Item iPad9thSpaceGrayWiFi64G = new Item.Builder(iPad9th).selections("스페이스 그레이,64GB X Wi-Fi").salesQuantity(777).id(12L).build(); // 12
        iPad9thSpaceGrayWiFi64G.addVendorItem(new VendorItem.Builder(iPad9thSpaceGrayWiFi64G, 449000).vendor(apple).id(13L).build()); // 13
        iPad9thSpaceGrayWiFi64G.addVendorItem(new VendorItem.Builder(iPad9thSpaceGrayWiFi64G, 449000).discountRate(4).vendor(coupang).id(14L).build()); // 14
        iPad9th.addItem(iPad9thSpaceGrayWiFi64G);

        Item iPad9thSilverWiFi256G = new Item.Builder(iPad9th).selections("실버,256GB X Wi-Fi").salesQuantity(97).id(15L).build(); // 15
        iPad9thSilverWiFi256G.addVendorItem(new VendorItem.Builder(iPad9thSilverWiFi256G, 639000).vendor(apple).id(16L).build()); // 16
        iPad9thSilverWiFi256G.addVendorItem(new VendorItem.Builder(iPad9thSilverWiFi256G, 639000).discountRate(3).vendor(coupang).id(17L).build()); // 17
        iPad9th.addItem(iPad9thSilverWiFi256G);

        return iPad9th;
    }
}
