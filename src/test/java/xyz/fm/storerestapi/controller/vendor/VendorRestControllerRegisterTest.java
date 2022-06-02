package xyz.fm.storerestapi.controller.vendor;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.vendor.VendorManagerJoinRequest;
import xyz.fm.storerestapi.dto.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.entity.Address;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.Role;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateVendorException;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateVendorManagerException;
import xyz.fm.storerestapi.exception.entity.notfound.VendorNotFoundException;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicatePhoneException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorRestControllerRegisterTest extends VendorRestControllerTest {

    @Test
    void registerVendor_400_invalidRequestValue() throws Exception {
        //given
        VendorRegisterRequest request = buildVendorRegisterRequest("executive", "01012345---", "password", "password");

        //when
        ResultActions ra = performRegisterVendor(request);

        //then
        assertErrorResponse(ra, ErrorCode.INVALID_ARGUMENT);
        ra
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    private VendorRegisterRequest buildVendorRegisterRequest(String email, String phone, String pwd, String confirmPwd) {
        return new VendorRegisterRequest(
                "vendor",
                "111",
                "ceo",
                new Address("zipcode", "base address", "detail address"),
                new VendorRegisterRequest.VendorExecutive(
                        new Email(email),
                        "executive",
                        new Phone(phone),
                        new Password(pwd),
                        new Password(confirmPwd)
                )
        );
    }

    public VendorRegisterRequest buildVendorRegisterRequest() {
        return buildVendorRegisterRequest("executive@vendor.com", "01012345678", "password", "password");
    }

    private ResultActions performRegisterVendor(VendorRegisterRequest request) throws Exception {
        String URL = "/vendor";
        return mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());
    }

    private void assertErrorResponse(ResultActions ra, ErrorCode errorCode) throws Exception {
        ra
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.error").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
    }

    @Test
    void registerVendor_400_pwdNotEqualToConfirmPwd() throws Exception {
        //given
        VendorRegisterRequest request = buildVendorRegisterRequest("executive@vendor.com", "01012345678", "password", "confirmPassword");

        //when
        ResultActions ra = performRegisterVendor(request);

        //then
        assertErrorResponse(ra, ErrorCode.PWD_NOT_EQUAL_TO_CONFIRM_PWD);
    }

    @Test
    void registerVendor_409_duplicateVendorName() throws Exception {
        //given
        VendorRegisterRequest request = buildVendorRegisterRequest();
        ErrorCode errorCode = ErrorCode.DUPLICATE_VENDOR_NAME;
        serviceRegisterVendorThrowEx(new DuplicateVendorException(errorCode));

        //when
        ResultActions ra = performRegisterVendor(request);

        //then
        assertErrorResponse(ra, errorCode);
    }

    private void serviceRegisterVendorThrowEx(Throwable throwable) {
        given(vendorService.registerVendor(any(Vendor.class), any(VendorManager.class)))
                .willThrow(throwable);
    }

    @Test
    void registerVendor_409_duplicateVendorRegNumber() throws Exception {
        //given
        VendorRegisterRequest request = buildVendorRegisterRequest();
        ErrorCode errorCode = ErrorCode.DUPLICATE_VENDOR_REG_NUMBER;
        serviceRegisterVendorThrowEx(new DuplicateVendorException(errorCode));

        //when
        ResultActions ra = performRegisterVendor(request);

        //then
        assertErrorResponse(ra, errorCode);
    }

    @Test
    void registerVendor_409_duplicateExecutiveEmail() throws Exception {
        //given
        VendorRegisterRequest request = buildVendorRegisterRequest();
        ErrorCode errorCode = ErrorCode.DUPLICATE_EMAIL;
        serviceRegisterVendorThrowEx(new DuplicateVendorException(errorCode));

        //when
        ResultActions ra = performRegisterVendor(request);

        //then
        assertErrorResponse(ra, errorCode);
    }

    @Test
    void registerVendor_409_duplicateExecutivePhone() throws Exception {
        //given
        VendorRegisterRequest request = buildVendorRegisterRequest();
        ErrorCode errorCode = ErrorCode.DUPLICATE_PHONE;
        Email existsEmail = new Email("existsEmail@test.com");
        serviceRegisterVendorThrowEx(new DuplicatePhoneException(errorCode, existsEmail));

        //when
        ResultActions ra = performRegisterVendor(request);

        //then
        assertErrorResponse(ra, errorCode);
        ra.andExpect(jsonPath("$.registeredEmail").value(existsEmail.encrypt()));
    }

    @Test
    void registerVendor_201() throws Exception {
        //given
        VendorRegisterRequest request = buildVendorRegisterRequest();
        Vendor vendor = buildVendor(request.getName(), request.getRegNumber(), request.getCeo(), request.getLocation(), 1L);

        given(vendorService.registerVendor(any(Vendor.class), any(VendorManager.class)))
                .willReturn(vendor);

        //when
        ResultActions ra = performRegisterVendor(request);

        //then
        ra
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(vendor.getId()))
                .andExpect(jsonPath("$.name").value(vendor.getName()))
                .andExpect(jsonPath("$.ceo").value(vendor.getCeo()))
                .andExpect(jsonPath("$.regNumber").value(vendor.getRegNumber()))
                .andExpect(jsonPath("$.location.zipcode").value(vendor.getLocation().getZipcode()))
                .andExpect(jsonPath("$.location.base").value(vendor.getLocation().getBase()))
                .andExpect(jsonPath("$.location.detail").value(vendor.getLocation().getDetail()));
    }

    @Test
    void joinVendorManager_400_PwdNotEqualToConfirmPwd() throws Exception {
        //given
        VendorManagerJoinRequest request = buildVendorManagerRegisterRequest("password", "confirmPassword");

        //when
        ResultActions ra = performJoinVendorManager(request);

        //then
        assertErrorResponse(ra, ErrorCode.PWD_NOT_EQUAL_TO_CONFIRM_PWD);
    }

    @Test
    void joinVendorManager_404_NotFoundVendor() throws Exception {
        //given
        VendorManagerJoinRequest request = buildVendorManagerRegisterRequest();
        given(vendorService.joinVendorManager(anyLong(), any(VendorManager.class)))
                .willThrow(new VendorNotFoundException(ErrorCode.VENDOR_NOT_FOUND));

        //when
        ResultActions ra = performJoinVendorManager(request);

        //then
        assertErrorResponse(ra, ErrorCode.VENDOR_NOT_FOUND);
    }

    @Test
    void joinVendorManager_409_duplicateEmail() throws Exception {
        //given
        VendorManagerJoinRequest request = buildVendorManagerRegisterRequest();
        given(vendorService.joinVendorManager(anyLong(), any(VendorManager.class)))
                .willThrow(new DuplicateVendorManagerException(ErrorCode.DUPLICATE_EMAIL));

        //when
        ResultActions ra = performJoinVendorManager(request);

        //then
        assertErrorResponse(ra, ErrorCode.DUPLICATE_EMAIL);
    }

    @Test
    void joinVendorManager_409_duplicatePhone() throws Exception {
        //given
        VendorManagerJoinRequest request = buildVendorManagerRegisterRequest();
        given(vendorService.joinVendorManager(anyLong(), any(VendorManager.class)))
                .willThrow(new DuplicatePhoneException(ErrorCode.DUPLICATE_PHONE, request.getEmail()));

        //when
        ResultActions ra = performJoinVendorManager(request);

        //then
        assertErrorResponse(ra, ErrorCode.DUPLICATE_PHONE);
        ra
                .andExpect(jsonPath("$.registeredEmail").value(request.getEmail().encrypt()));
    }

    @Test
    void joinVendorManager_201() throws Exception {
        //given
        VendorManagerJoinRequest request = buildVendorManagerRegisterRequest();
        Vendor vendor = buildVendor("vendor", "1", "ceo", new Address("zipcode", "base", "detail"), 1L);

        VendorManager staff = VendorManager.builder()
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .password(request.getPassword())
                .id(2L)
                .role(Role.ROLE_VENDOR_STAFF)
                .build();

        vendor.addManager(staff);

        given(vendorService.joinVendorManager(anyLong(), any(VendorManager.class)))
                .willReturn(staff);

        //when
        ResultActions ra = performJoinVendorManager(request);

        //then
        ra
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vendorManagerId").value(staff.getId()))
                .andExpect(jsonPath("$.vendorId").value(staff.getVendor().getId()))
                .andExpect(jsonPath("$.email").value(staff.getEmail().toString()))
                .andExpect(jsonPath("$.name").value(staff.getName()))
                .andExpect(jsonPath("$.phone").value(staff.getPhone().toString()))
                .andExpect(jsonPath("$.approved").value(false))
                .andExpect(jsonPath("$.approvalManagerId").doesNotExist())
                .andExpect(jsonPath("$.role").value("ROLE_VENDOR_STAFF"));
    }

    private Vendor buildVendor(String name, String regNumber, String ceo, Address location, Long id) {
        return Vendor.builder()
                .name(name)
                .regNumber(regNumber)
                .ceo(ceo)
                .location(location)
                .id(id)
                .build();
    }

    private VendorManagerJoinRequest buildVendorManagerRegisterRequest(String password, String confirmPassword) {
        return new VendorManagerJoinRequest(
                new Email("vendorManager@vendor.com"),
                "vendorManager",
                new Phone("01012345678"),
                new Password(password),
                new Password(confirmPassword),
                1L
        );
    }

    private VendorManagerJoinRequest buildVendorManagerRegisterRequest() {
        return buildVendorManagerRegisterRequest("password", "password");
    }

    private ResultActions performJoinVendorManager(VendorManagerJoinRequest request) throws Exception {
        return mvc.perform(
                MockMvcRequestBuilders.post("/vendor/manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());
    }
}
