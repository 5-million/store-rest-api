package xyz.fm.storerestapi.service.vendor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.entity.Address;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.Role;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.CustomException;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateEntityException;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateVendorManagerException;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicatePhoneException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VendorServiceRegisterTest extends VendorServiceTest {

    private Address location;
    private Vendor vendor;
    private VendorManager executive;

    @BeforeEach
    void beforeEach() {
        location = new Address("zipcode", "base address", "detail address");
        vendor = Vendor.builder()
                .name("store")
                .regNumber("1")
                .ceo("kim")
                .location(location)
                .build();

        executive = VendorManager.builder()
                .email(new Email("executive@vendor.com"))
                .name("executive")
                .phone(new Phone("01012345678"))
                .password(new Password("password"))
                .approved(true)
                .role(Role.ROLE_VENDOR_EXECUTIVE)
                .build();
    }

    @Test
    void registerVendor_fail_duplicateVendorName() {
        given(vendorRepository.existsByName(anyString())).willReturn(true);
        DuplicateEntityException exception = assertDuplicateEx();
        assertErrorCode(exception, ErrorCode.DUPLICATE_VENDOR_NAME);
    }

    @Test
    void registerVendor_fail_duplicateVendorRegNumber() {
        given(vendorRepository.existsByRegNumber(anyString())).willReturn(true);
        DuplicateEntityException exception = assertDuplicateEx();
        assertErrorCode(exception, ErrorCode.DUPLICATE_VENDOR_REG_NUMBER);
    }

    @Test
    void registerVendor_fail_duplicateVendorManagerEmail() {
        given(vendorManagerRepository.existsByEmail(any(Email.class))).willReturn(true);
        DuplicateEntityException exception = assertDuplicateEx();
        assertErrorCode(exception, ErrorCode.DUPLICATE_EMAIL);
    }

    @Test
    void registerVendor_fail_duplicateVendorManagerPhone() {
        given(vendorManagerRepository.findByPhone(any(Phone.class)))
                .willReturn(Optional.of(executive));
        DuplicatePhoneException exception =
                assertThrows(DuplicatePhoneException.class, () -> vendorService.registerVendor(vendor, executive));
        assertErrorCode(exception, ErrorCode.DUPLICATE_PHONE);
    }

    @Test
    void registerVendor_success() {
        given(vendorRepository.save(any(Vendor.class))).willReturn(vendor);

        Vendor registeredVendor = vendorService.registerVendor(vendor, executive);

        verify(passwordEncoder, times(1)).encode(any(CharSequence.class));
        verify(vendorRepository, times(1)).save(any(Vendor.class));
        assertThat(registeredVendor.getVendorManagerList().size()).isEqualTo(1);
        assertThat(registeredVendor.getVendorManagerList().get(0)).isEqualTo(executive);
        assertThat(registeredVendor.getVendorManagerList().get(0).getVendor()).isEqualTo(vendor);
    }

    @Test
    void duplicateCheckVendorManager_throw_DuplicateVendorManagerEx() throws Exception {
        //given
        VendorManager staff = buildVendorManager();

        given(vendorManagerRepository.existsByEmail(any(Email.class)))
                .willReturn(true);

        //when
        DuplicateVendorManagerException exception =
                assertThrows(DuplicateVendorManagerException.class, () -> vendorService.duplicateCheckVendorManager(staff));

        //then
        assertErrorCode(exception, ErrorCode.DUPLICATE_EMAIL);
    }

    @Test
    void duplicateCheckVendorManager_throw_DuplicatePhoneEx() throws Exception {
        //given
        VendorManager staff = buildVendorManager();

        given(vendorManagerRepository.findByPhone(any(Phone.class)))
                .willReturn(Optional.of(staff));

        //when
        DuplicatePhoneException exception =
                assertThrows(DuplicatePhoneException.class, () -> vendorService.duplicateCheckVendorManager(staff));

        //then
        assertErrorCode(exception, ErrorCode.DUPLICATE_PHONE);
    }

    @Test
    void duplicateCheckVendorManager_throw_none() throws Exception {
        VendorManager staff = buildVendorManager();
        vendorService.duplicateCheckVendorManager(staff);

        verify(vendorManagerRepository, times(1)).existsByEmail(any(Email.class));
        verify(vendorManagerRepository, times(1)).findByPhone(any(Phone.class));
    }

    @Test
    void registerVendorManager_success() throws Exception {
        //given
        VendorManager staff = buildVendorManager();
        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(vendor));

        //when
        VendorManager registeredManager = vendorService.joinVendorManager(1, staff);

        //then
        verify(vendorRepository, times(1)).findById(anyLong());
        assertThat(registeredManager).isEqualTo(staff);
        assertThat(registeredManager.getVendor()).isEqualTo(vendor);
        assertThat(vendor.getVendorManagerList().size()).isEqualTo(1);
    }

    private DuplicateEntityException assertDuplicateEx() {
        return assertThrows(DuplicateEntityException.class, () -> vendorService.registerVendor(vendor, executive));
    }

    private void assertErrorCode(CustomException exception, ErrorCode expected) {
        assertThat(exception.getErrorCode()).isEqualTo(expected);
    }

    private VendorManager buildVendorManager() {
        return VendorManager.builder()
                .email(new Email("vendorManager@vendor.com"))
                .name("vendorManager")
                .phone(new Phone("01012345678"))
                .password(new Password("password"))
                .role(Role.ROLE_VENDOR_STAFF)
                .build();
    }
}
