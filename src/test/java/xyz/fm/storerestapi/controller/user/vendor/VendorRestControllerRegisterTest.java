package xyz.fm.storerestapi.controller.user.vendor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import xyz.fm.storerestapi.dto.user.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.InvalidPasswordException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class VendorRestControllerRegisterTest extends VendorRestControllerTest {

    private final String url = BASE_URL;

    private VendorRegisterRequest buildTestRequest(String email, String phoneNumber, String password, String confirmPassword) {
        return new VendorRegisterRequest(
                "apple", "cook", "101", "usa",
                new VendorRegisterRequest.VendorAdmin(
                        email,
                        "admin",
                        password,
                        confirmPassword,
                        phoneNumber
                )
        );
    }

    private ResultActions performMvc(VendorRegisterRequest request) throws Exception {
        return mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());
    }

    @Test
    @DisplayName("register fail: not email format")
    public void register_fail_notEmailFormat() throws Exception {
        //given
        VendorRegisterRequest request = buildTestRequest("admin", "01012345678", "pwd", "pwd");

        //when
        ResultActions ra = performMvc(request);

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_EMAIL_FORMAT));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(MethodArgumentNotValidException.class);
    }

    @Test
    @DisplayName("register fail: not phone format")
    public void register_fail_notPhoneFormat() throws Exception {
        //given
        VendorRegisterRequest request = buildTestRequest("admin@test.com", "010", "pwd", "pwd");

        //when
        ResultActions ra = performMvc(request);

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_PHONE_NUMBER_FORMAT));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(MethodArgumentNotValidException.class);
    }

    @Test
    @DisplayName("register fail: pwd != confirmPwd")
    public void register_fail_pwdNotEqualToConfirmPwd() throws Exception {
        //given
        VendorRegisterRequest request = buildTestRequest("admin@test.com", "01012345678", "pwd", "qwe");
        given(vendorService.register(any(VendorRegisterRequest.class)))
                .willThrow(new InvalidPasswordException(Error.INVALID_PASSWORD, ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD));

        //when
        ResultActions ra = performMvc(request);

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.INVALID_PASSWORD.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(InvalidPasswordException.class);
    }

    @Test
    @DisplayName("register fail: duplicate vendor")
    public void register_fail_duplicateVendor() throws Exception {
        //given
        VendorRegisterRequest request = buildTestRequest("admin@test.com", "01012345678", "pwd", "pwd");
        given(vendorService.register(any(VendorRegisterRequest.class)))
                .willThrow(new DuplicationException(Error.DUPLICATE, ErrorDetail.DUPLICATE_VENDOR));

        //when
        ResultActions ra = performMvc(request);

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value(Error.DUPLICATE.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.DUPLICATE_VENDOR));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(DuplicationException.class);
    }

    @Test
    @DisplayName("register success")
    public void register_success() throws Exception {
        //given
        VendorRegisterRequest request = buildTestRequest("admin@test.com", "01012345678", "pwd", "pwd");
        given(vendorService.register(any(VendorRegisterRequest.class))).willReturn(any(Vendor.class));

        //when
        ResultActions ra = performMvc(request);

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(status().isCreated());
    }
}
