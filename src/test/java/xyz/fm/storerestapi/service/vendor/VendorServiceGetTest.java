package xyz.fm.storerestapi.service.vendor;

import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.notfound.VendorNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.VENDOR_NOT_FOUND);
    }
}
