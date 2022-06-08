package xyz.fm.storerestapi.controller.category;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.controller.TestJwtFactory;
import xyz.fm.storerestapi.dto.category.CategoryRegisterRequest;
import xyz.fm.storerestapi.entity.Category;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.Role;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateCategoryException;
import xyz.fm.storerestapi.exception.entity.notfound.CategoryNotFoundException;
import xyz.fm.storerestapi.jwt.JwtAuthenticationFilter;
import xyz.fm.storerestapi.jwt.JwtProvider;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CategoryRestControllerRegisterTest extends CategoryRestControllerTest {

    private static final String URL = BASE_URL;
    private final String accessToken;

    public CategoryRestControllerRegisterTest(@Autowired JwtProvider jwtProvider) {
        VendorManager staff = VendorManager.builder()
                .id(1L)
                .email(new Email("staff@vendor.com"))
                .name("staff")
                .password(new Password("password"))
                .phone(new Phone("01012345678"))
                .role(Role.ROLE_VENDOR_STAFF)
                .approved(true)
                .build();

        accessToken = new TestJwtFactory(jwtProvider).buildAccessToken(staff);
    }

    @Test
    void register_404() throws Exception {
        //given
        ErrorCode errorCode = ErrorCode.CATEGORY_NOT_FOUND;
        CategoryRegisterRequest request = buildTestRequest();
        givenCategoryServiceRegister().willThrow(new CategoryNotFoundException(errorCode));

        //when
        ResultActions ra = performRegister(request);

        //then
        assertErrorCode(ra, errorCode);
    }

    @Test
    void register_409() throws Exception {
        //given
        ErrorCode errorCode = ErrorCode.DUPLICATE_CATEGORY;
        CategoryRegisterRequest request = buildTestRequest();
        givenCategoryServiceRegister().willThrow(new DuplicateCategoryException(errorCode));

        //when
        ResultActions ra = performRegister(request);

        //then
        assertErrorCode(ra, errorCode);
    }

    @Test
    void register_201() throws Exception {
        //given
        CategoryRegisterRequest request = buildTestRequest();
        Category parent = Category.builder()
                .id(request.getParentId())
                .name("parent")
                .build();

        Category child = Category.builder()
                .id(parent.getId() + 1)
                .name("child")
                .parent(parent)
                .build();

        givenCategoryServiceRegister().willReturn(child);

        //when
        ResultActions ra = performRegister(request);

        //then
        ra
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/categories/2"))
                .andExpect(jsonPath("$.categoryId").value(child.getId()))
                .andExpect(jsonPath("$.categoryName").value(child.getName()))
                .andExpect(jsonPath("$.depth").value(child.getDepth()))
                .andExpect(jsonPath("$.parentId").value(parent.getId()));
    }

    private ResultActions performRegister(CategoryRegisterRequest request) throws Exception {
        return mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .header(JwtAuthenticationFilter.AUTHORIZATION_HEADER, JwtAuthenticationFilter.BEARER_PREFIX + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());
    }

    private CategoryRegisterRequest buildTestRequest() {
        return new CategoryRegisterRequest(1L, "category");
    }


    private BDDMockito.BDDMyOngoingStubbing<Category> givenCategoryServiceRegister() {
        return given(categoryService.register(anyLong(), anyString()));
    }

    private void assertErrorCode(ResultActions ra, ErrorCode errorCode) throws Exception {
        ra
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.error").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
    }
}
