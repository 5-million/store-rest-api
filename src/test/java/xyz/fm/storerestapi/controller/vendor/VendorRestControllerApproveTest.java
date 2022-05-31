package xyz.fm.storerestapi.controller.vendor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.controller.TestJwtFactory;
import xyz.fm.storerestapi.entity.Address;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.nopermission.VendorManagerAuthorityException;
import xyz.fm.storerestapi.exception.entity.notfound.VendorManagerNotFoundException;
import xyz.fm.storerestapi.jwt.JwtAuthenticationFilter;
import xyz.fm.storerestapi.jwt.JwtProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorRestControllerApproveTest extends VendorRestControllerTest {

    private static final Long staffId = 2L;
    private static final String URL = "/vendor/manager/approve/" + staffId;

    private final TestJwtFactory testJwtFactory;
    private VendorManager executive, staff;
    private String executiveAccessToken;

    public VendorRestControllerApproveTest(@Autowired JwtProvider jwtProvider) {
        testJwtFactory = new TestJwtFactory(jwtProvider);
    }

    @BeforeEach
    void beforeEach() {
        Vendor vendor = new Vendor.Builder(
                "vendor",
                "1",
                "ceo",
                new Address("zipcode", "base", "detail")
        ).id(0L).build();

        executive = new VendorManager.Builder(
                new Email("executive@vendor.com"),
                "executive",
                new Phone("01012345678"),
                new Password("password")
        ).id(1L).approved(true).buildExecutive();

        staff = new VendorManager.Builder(
                new Email("staff@vendor.com"),
                "staff",
                new Phone("01012345679"),
                new Password("password")
        ).id(staffId).buildStaff();

        executiveAccessToken = testJwtFactory.buildAccessToken(executive);
    }

    @Test
    void approveManager_401() throws Exception {
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(URL)
        ).andDo(print());

        assertErrorCode(ra, ErrorCode.JWT_UNAUTHORIZED);
    }

    @Test
    void approveManager_403_causeJWT() throws Exception {
        String staffAccessToken = testJwtFactory.buildAccessToken(staff);

        ResultActions ra = performApproveManager(staffAccessToken);

        assertErrorCode(ra, ErrorCode.JWT_NO_PERMISSION);
    }

    @Test
    void approveManager_403_causeNotApproved() throws Exception {
        ErrorCode errorCode = ErrorCode.NOT_APPROVED_VENDOR_MANAGER;
        approveManagerDoThrow(errorCode);
        givenGetVendorManager();

        ResultActions ra = performApproveManager(executiveAccessToken);

        assertErrorCode(ra, errorCode);
    }

    @Test
    void approveManager_403_causeNotExecutive() throws Exception {
        ErrorCode errorCode = ErrorCode.REQUIRE_MORE_THEN_EXECUTIVE_ROLE;
        approveManagerDoThrow(errorCode);
        givenGetVendorManager();

        ResultActions ra = performApproveManager(executiveAccessToken);

        assertErrorCode(ra, errorCode);
    }

    @Test
    void approveManager_403_causeNotSameVendor() throws Exception {
        ErrorCode errorCode = ErrorCode.NOT_SAME_VENDOR;
        approveManagerDoThrow(errorCode);
        givenGetVendorManager();

        ResultActions ra = performApproveManager(executiveAccessToken);

        assertErrorCode(ra, errorCode);
    }

    @Test
    void approveManager_404_NotFoundExecutive() throws Exception {
        ErrorCode errorCode = ErrorCode.VENDOR_NOT_FOUND;
        given(vendorService.getVendorManagerByEmail(any(Email.class)))
                .willThrow(new VendorManagerNotFoundException(errorCode));

        ResultActions ra = performApproveManager(executiveAccessToken);

        assertErrorCode(ra, errorCode);
    }

    @Test
    void approveManager_404_NotFoundTarget() throws Exception {
        ErrorCode errorCode = ErrorCode.VENDOR_NOT_FOUND;
        given(vendorService.getVendorManagerById(anyLong()))
                .willThrow(new VendorManagerNotFoundException(errorCode));

        ResultActions ra = performApproveManager(executiveAccessToken);

        assertErrorCode(ra, errorCode);
    }

    @Test
    void approveManager_200() throws Exception {
        ResultActions ra = performApproveManager(executiveAccessToken);
        ra.andExpect(status().isOk());
    }

    private ResultActions performApproveManager(String accessToken) throws Exception {
        return mvc.perform(
                MockMvcRequestBuilders.patch(URL)
                        .header(
                                JwtAuthenticationFilter.AUTHORIZATION_HEADER,
                                JwtAuthenticationFilter.BEARER_PREFIX + accessToken
                        )
        ).andDo(print());
    }

    private void assertErrorCode(ResultActions ra, ErrorCode errorCode) throws Exception {
        ra
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.error").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
    }

    private void approveManagerDoThrow(ErrorCode errorCode) {
        doThrow(new VendorManagerAuthorityException(errorCode))
                .when(vendorService)
                .approveManager(any(VendorManager.class), any(VendorManager.class));
    }

    private void givenGetVendorManager() {
        given(vendorService.getVendorManagerByEmail(any(Email.class)))
                .willReturn(executive);

        given(vendorService.getVendorManagerById(anyLong()))
                .willReturn(staff);
    }

    @Test
    void test() throws Exception {
        ErrorCode errorCode = ErrorCode.NOT_SAME_VENDOR;
        approveManagerDoThrow(errorCode);

        VendorManagerAuthorityException exception =
                Assertions.assertThrows(VendorManagerAuthorityException.class, () -> vendorService.approveManager(
                        executive, staff
                ));

        Assertions.assertEquals(errorCode, exception.getErrorCode());
    }
}
