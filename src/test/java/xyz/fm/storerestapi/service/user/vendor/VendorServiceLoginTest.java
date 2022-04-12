package xyz.fm.storerestapi.service.user.vendor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.LoginException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.util.EncryptUtil;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class VendorServiceLoginTest extends VendorServiceTest {

    @Test
    @DisplayName("login fail: not found manager")
    public void login_fail_notFoundManager() throws Exception {
        //given
        LoginRequest request = new LoginRequest("manager@vendor.com", "password");
        given(vendorManagerRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> vendorService.login(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_USER);
        assertThat(exception.isDetailIgnoreForUser()).isTrue();
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
        ).permission(true).build();

        given(vendorManagerRepository.findByEmail(anyString())).willReturn(Optional.of(manager));

        //when
        LoginException exception = assertThrows(LoginException.class, () -> vendorService.login(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.LOGIN_FAIL);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.INCORRECT_PWD);
        assertThat(exception.isDetailIgnoreForUser()).isTrue();
    }

    @Test
    @DisplayName("login fail: not approved")
    public void login_fail_notApproved() throws Exception {
        //given
        String email = "manager@vendor.com";
        LoginRequest request = new LoginRequest(email, "password");

        VendorManager manager = new VendorManager.Builder(
                email,
                "manager",
                EncryptUtil.encode("password"),
                "01012345678",
                null
        ).permission(false).build();

        given(vendorManagerRepository.findByEmail(anyString())).willReturn(Optional.of(manager));

        //when
        LoginException exception = assertThrows(LoginException.class, () -> vendorService.login(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_APPROVED);
    }

    @Test
    @DisplayName("login success")
    public void login_success() throws Exception {
        //given
        String email = "manager@vendor.com";
        LoginRequest request = new LoginRequest(email, "password");

        VendorManager manager = new VendorManager.Builder(
                email,
                "manager",
                EncryptUtil.encode("password"),
                "01012345678",
                null
        ).permission(true).build();

        given(vendorManagerRepository.findByEmail(anyString())).willReturn(Optional.of(manager));

        //when
        VendorManager loginManager = vendorService.login(request);

        //then
        assertThat(loginManager).isEqualTo(manager);
        assertThat(loginManager.getLastLoginDate()).isNotNull();
    }
}
