package xyz.fm.storerestapi.service.user.vendor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerJoinRequest;
import xyz.fm.storerestapi.dto.user.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.entity.user.vendor.VendorRole;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.InvalidPasswordException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.util.EncryptUtil;

import javax.persistence.PersistenceException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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

    @Test
    @DisplayName("joinManager fail: pwd not equal to confirmPwd")
    public void joinManager_fail_pwdNotEqualToConfirmPwd() throws Exception {
        //given
        VendorManagerJoinRequest request = new VendorManagerJoinRequest(
                1L, // vendorId
                "manager@vendor.com",
                "manager",
                "pwd",
                "qwe",
                "01012345678"
        );

        //when
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> vendorService.joinManager(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.INVALID_PASSWORD);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD);
    }

    @Test
    @DisplayName("joinManager fail: not found vendor")
    public void joinManager_fail_notFoundVendor() throws Exception {
        //given
        VendorManagerJoinRequest request = new VendorManagerJoinRequest(
                1L, // vendorId
                "manager@vendor.com",
                "manager",
                "pwd",
                "pwd",
                "01012345678"
        );

        given(vendorRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> vendorService.joinManager(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_VENDOR);
    }

    @Test
    @DisplayName("joinManager fail: conflict")
    public void joinManger_fail_duplicateEmail() throws Exception {
        //given
        VendorManagerJoinRequest request = new VendorManagerJoinRequest(
                1L, // vendorId
                "manager@vendor.com",
                "manager",
                "pwd",
                "pwd",
                "01012345678"
        );

        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(
                new Vendor.Builder("apple", "cook", "010", "usa")
                        .id(1L)
                        .build()
        ));
        given(vendorManagerRepository.save(any(VendorManager.class))).willThrow(PersistenceException.class);

        //when
        DuplicationException exception = assertThrows(DuplicationException.class, () -> vendorService.joinManager(request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.DUPLICATE);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.DUPLICATE_USER);
    }

    @Test
    @DisplayName("joinManager success")
    public void joinManager_success() throws Exception {
        //given
        VendorManagerJoinRequest request = new VendorManagerJoinRequest(
                1L, // vendorId
                "manager@vendor.com",
                "manager",
                "pwd",
                "pwd",
                "01012345678"
        );

        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa")
                .id(1L)
                .build();

        VendorManager manager = new VendorManager.Builder(
                request.getEmail(),
                request.getName(),
                EncryptUtil.encode(request.getPassword()),
                request.getPhoneNumber(),
                vendor
        ).build();

        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(vendor));
        given(vendorManagerRepository.save(any(VendorManager.class))).willReturn(manager);

        //when
        VendorManager registeredManager = vendorService.joinManager(request);

        //then
        assertThat(registeredManager.getVendor().getId()).isEqualTo(vendor.getId());
        assertThat(registeredManager.getRole()).isEqualTo(VendorRole.VENDOR_MANAGER);
        assertThat(registeredManager.getPermission()).isFalse();
    }
}
