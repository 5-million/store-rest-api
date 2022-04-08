package xyz.fm.storerestapi.service.user.vendor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.user.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.entity.user.vendor.VendorRole;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.InvalidPasswordException;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static xyz.fm.storerestapi.dto.user.vendor.VendorRegisterRequest.VendorAdmin;

public class VendorServiceRegisterTest extends VendorServiceTest {

    @Test
    @DisplayName("register fail: pwd != confirmPwd")
    public void register_fail_pwdNotEqualToConfirmPwd() throws Exception {
        //given
        VendorRegisterRequest request = new VendorRegisterRequest(
                "apple", "cook", "101", "usa",
                new VendorAdmin(
                        "admin@test.com",
                        "admin",
                        "pwd",
                        "qwe",
                        "01012345678"
                )
        );

        //when
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> vendorService.register(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.INVALID_PASSWORD);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD);

    }

    @Test
    @DisplayName("register fail: duplicate registration number")
    public void register_fail_duplicateRegistrationNumber() throws Exception {
        //given
        VendorRegisterRequest request = new VendorRegisterRequest(
                "apple", "cook", "101", "usa",
                new VendorAdmin(
                        "admin@test.com",
                        "admin",
                        "pwd",
                        "pwd",
                        "01012345678"
                )
        );

        given(vendorRepository.existsByRegistrationNumber(anyString())).willReturn(true);

        //when
        DuplicationException exception = assertThrows(DuplicationException.class, () -> vendorService.register(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.DUPLICATE);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.DUPLICATE_VENDOR);
    }

    @Test
    @DisplayName("register fail: register conflict")
    public void register_fail_registerConflict() throws Exception {
        //given
        VendorRegisterRequest request = new VendorRegisterRequest(
                "apple", "cook", "101", "usa",
                new VendorAdmin(
                        "admin@test.com",
                        "admin",
                        "pwd",
                        "pwd",
                        "01012345678"
                )
        );

        given(vendorRepository.save(any(Vendor.class))).willThrow(PersistenceException.class);

        //when
        DuplicationException exception = assertThrows(DuplicationException.class, () -> vendorService.register(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.DUPLICATE);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.DUPLICATE_VENDOR);
    }

    @Test
    @DisplayName("register success")
    public void register_success() throws Exception {
        //given
        VendorRegisterRequest request = new VendorRegisterRequest(
                "apple", "cook", "101", "usa",
                new VendorAdmin(
                        "admin@test.com",
                        "admin",
                        "pwd",
                        "pwd",
                        "01012345678"
                )
        );

        given(vendorRepository.existsByRegistrationNumber(anyString())).willReturn(false);
        given(vendorRepository.save(any(Vendor.class))).willReturn(any(Vendor.class));

        //when
        Vendor result = vendorService.register(request);

        //then
        assertThat(result.getManagers().size()).isEqualTo(1);
        assertThat(result.getManagers().get(0).getRole()).isEqualTo(VendorRole.VENDOR_ROOT);
        assertThat(result.getManagers().get(0).getPermission()).isEqualTo(true);
    }
}
