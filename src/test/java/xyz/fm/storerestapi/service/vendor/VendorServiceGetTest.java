package xyz.fm.storerestapi.service.vendor;

import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.notfound.EntityNotFoundException;
import xyz.fm.storerestapi.exception.entity.notfound.VendorManagerNotFoundException;
import xyz.fm.storerestapi.exception.entity.notfound.VendorNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

public class VendorServiceGetTest extends VendorServiceTest {

    @Test
    void getVendorById_throw_VendorNotFoundEx() throws Exception {
        //given
        given(vendorRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        VendorNotFoundException exception = assertThrows(VendorNotFoundException.class, () -> vendorService.getVendorById(1));

        //then
        assertErrorCode(exception, ErrorCode.VENDOR_NOT_FOUND);
    }

    private void assertErrorCode(EntityNotFoundException exception, ErrorCode errorCode) {
        assertThat(exception.getErrorCode()).isEqualTo(errorCode);
    }

    @Test
    void getVendorManagerByEmail_throw_VendorManagerNotFoundEx() throws Exception {
        //given
        given(vendorManagerRepository.findByEmail(any(Email.class))).willReturn(Optional.empty());

        //when
        VendorManagerNotFoundException exception =
                assertThrows(
                        VendorManagerNotFoundException.class,
                        () -> vendorService.getVendorManagerByEmail(new Email("vm@vendor.com"))
                );

        //then
        assertErrorCode(exception, ErrorCode.VENDOR_MANAGER_NOT_FOUND);
    }

    @Test
    void getVendorManagerById_throw_VendorManagerNotFoundEx() throws Exception {
        //given
        given(vendorManagerRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        VendorManagerNotFoundException exception =
                assertThrows(
                        VendorManagerNotFoundException.class,
                        () -> vendorService.getVendorManagerById(1L)
                );

        //then
        assertErrorCode(exception, ErrorCode.VENDOR_MANAGER_NOT_FOUND);
    }
}
