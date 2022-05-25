package xyz.fm.storerestapi.controller.vendor;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.entity.Address;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateVendorException;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicatePhoneException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorRestControllerRegisterTest extends VendorRestControllerTest {

    @Test
    void registerVendor_400_invalidRequestValue() throws Exception {
        //given
        VendorRegisterRequest request = buildVendorRegisterRequest("executive", "01012345", "password", "password");

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
        Vendor vendor = new Vendor.Builder(
                request.getName(),
                request.getRegNumber(),
                request.getCeo(),
                request.getLocation()
        ).id(1L).build();

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
}
