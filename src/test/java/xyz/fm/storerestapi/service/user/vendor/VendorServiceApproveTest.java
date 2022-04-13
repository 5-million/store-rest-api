package xyz.fm.storerestapi.service.user.vendor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerApproveRequest;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerApproveResult;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.entity.user.vendor.VendorRole;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NoPermissionException;
import xyz.fm.storerestapi.error.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class VendorServiceApproveTest extends VendorServiceTest {

    @Test
    @DisplayName("approve fail: not found admin")
    public void approve_fail_notFoundAdmin() throws Exception {
        //given
        List<String> targets = new ArrayList<>();
        String adminEmail = "admin@vendor.com";
        VendorManagerApproveRequest request = new VendorManagerApproveRequest(targets);

        given(vendorManagerRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> vendorService.approve(adminEmail, request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_USER);
    }

    @Test
    @DisplayName("approve fail: not admin")
    public void approve_fail_notAdmin() throws Exception {
        //given
        List<String> targets = new ArrayList<>();

        VendorManager admin = new VendorManager.Builder(
                "admin@vendor.com",
                "admin",
                "password",
                "01012345678",
                null
        ).build();

        VendorManagerApproveRequest request = new VendorManagerApproveRequest(targets);

        given(vendorManagerRepository.findByEmail(anyString())).willReturn(Optional.of(admin));
        
        //when
        NoPermissionException exception = assertThrows(NoPermissionException.class, () -> vendorService.approve(admin.getEmail(), request));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NO_PERMISSION);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_ADMIN);
    }

    @Test
    @DisplayName("approve success")
    public void approve_success() throws Exception {
        //given
        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").build();

        VendorManager admin = new VendorManager.Builder(
                "admin@vendor.com",
                "admin",
                "password",
                "01012345678",
                vendor
        ).role(VendorRole.VENDOR_ROOT).permission(true).build();

        given(vendorManagerRepository.findByEmail(anyString())).willReturn(Optional.of(admin));

        VendorManager manager1 = new VendorManager.Builder(
                "manager1@vendor.com",
                "manager1",
                "password",
                "01012345677",
                vendor
        ).build();

        VendorManager manager2 = new VendorManager.Builder(
                "manager2@vendor.com",
                "manager2",
                "password",
                "01012345677",
                vendor
        ).permission(true).build();

        vendor.addManager(admin);
        vendor.addManager(manager1);
        vendor.addManager(manager2);

        List<VendorManager> managers = new ArrayList<>();
        managers.add(manager1);
        managers.add(manager2);
        given(vendorManagerRepository.findByVendorAndEmailIn(any(Vendor.class), any(List.class))).willReturn(managers);

        List<String> targets = new ArrayList<>();
        targets.add(manager1.getEmail());
        targets.add(manager2.getEmail());
        targets.add("abc@test.com");
        VendorManagerApproveRequest request = new VendorManagerApproveRequest(targets);

        //when
        VendorManagerApproveResult result = vendorService.approve(admin.getEmail(), request);

        //then
        assertThat(result.getSuccess().getSize()).isEqualTo(1);
        assertThat(result.getFail().getSize()).isEqualTo(2);
    }
}
