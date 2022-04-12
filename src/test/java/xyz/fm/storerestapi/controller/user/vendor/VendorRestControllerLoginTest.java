package xyz.fm.storerestapi.controller.user.vendor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.LoginException;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;
import xyz.fm.storerestapi.util.EncryptUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class VendorRestControllerLoginTest extends VendorRestControllerTest {
    
    private final String loginUrl = BASE_URL + "/manager/login";

    @Test
    @DisplayName("login fail: not found user")
    public void login_fail_notFoundUser() throws Exception {
        //given
        LoginRequest request = new LoginRequest("manager@vendor.com", "password");
        given(vendorService.login(any(LoginRequest.class)))
                .willThrow(new LoginException(Error.LOGIN_FAIL, ErrorDetail.NOT_FOUND_USER, true));
        
        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.LOGIN_FAIL.getCode()))
                .andExpect(jsonPath("$.detail").value(""));

        Exception exception = ra.andReturn().getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception.getClass()).isEqualTo(LoginException.class);
        assertThat(((LoginException) exception).getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_USER);
    }
    
    @Test
    @DisplayName("login fail: incorrect password")
    public void login_fail_incorrectPassword() throws Exception {
        //given
        String email = "manager@vendor.com";
        LoginRequest request = new LoginRequest(email, "incorrectPwd");
        VendorManager manager = new VendorManager.Builder(
                email,
                "manager",
                EncryptUtil.encode("password"),
                "01012345678",
                null
        ).build();

        given(vendorService.login(any(LoginRequest.class)))
                .willThrow(new LoginException(Error.LOGIN_FAIL, ErrorDetail.INCORRECT_PWD, true));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.LOGIN_FAIL.getCode()))
                .andExpect(jsonPath("$.detail").value(""));

        Exception exception = ra.andReturn().getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception.getClass()).isEqualTo(LoginException.class);
        assertThat(((LoginException) exception).getDetail()).isEqualTo(ErrorDetail.INCORRECT_PWD);
    }
    
    @Test
    @DisplayName("login fail: not approved")
    public void login_fail_notApproved() throws Exception {
        //given
        String email = "manager@vendor.com";
        LoginRequest request = new LoginRequest(email, "password");

        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").build();
        VendorManager manager = new VendorManager.Builder(
                email,
                "manager",
                EncryptUtil.encode("password"),
                "01012345678",
                vendor
        ).id(1L).permission(false).build();

        given(vendorService.login(any(LoginRequest.class))).willThrow(new LoginException(Error.NOT_APPROVED));
        
        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());
        
        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_APPROVED.getCode()));

        Exception exception = ra.andReturn().getResolvedException();
        assertThat(exception).isNotNull();
        assertThat(exception.getClass()).isEqualTo(LoginException.class);
    }

    @Test
    @DisplayName("login success")
    public void login_success() throws Exception {
        //given
        String email = "manager@vendor.com";
        LoginRequest request = new LoginRequest(email, "password");

        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").build();
        VendorManager manager = new VendorManager.Builder(
                email,
                "manager",
                EncryptUtil.encode("password"),
                "01012345678",
                vendor
        ).id(1L).permission(true).build();

        given(vendorService.login(any(LoginRequest.class))).willReturn(manager);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.managerId").value(manager.getId()))
                .andExpect(jsonPath("$.email").value(manager.getEmail()))
                .andExpect(jsonPath("$.name").value(manager.getName()))
                .andExpect(jsonPath("$.phoneNumber").value(manager.getPhoneNumber()))
                .andExpect(jsonPath("$.permission").value(manager.getPermission()))
                .andExpect(jsonPath("$.role").value(manager.getRole().getRole()))
                .andExpect(jsonPath("$.vendorId").value(manager.getVendor().getId()))
                .andExpect(jsonPath("$.vendorName").value(manager.getVendor().getVendorName()))
                .andExpect(cookie().exists(JwtTokenUtil.JWT_KEY));
    }
}
