package xyz.fm.storerestapi.entity.user.vendor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.entity.Address;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.Role;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.nopermission.VendorManagerAuthorityException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VendorManagerTest {

    private Vendor vendor;
    private VendorManager executive;
    private VendorManager staff;

    @BeforeEach
    void beforeEach() {
        vendor = Vendor.builder()
                .name("vendor")
                .regNumber("1")
                .ceo("ceo")
                .location(new Address("zipcode", "base", "detail"))
                .id(1L)
                .build();

        executive = VendorManager.builder()
                .email(new Email("executive"))
                .name("executive")
                .phone(new Phone("01012345678"))
                .password(new Password("password"))
                .id(2L)
                .approved(true)
                .role(Role.ROLE_VENDOR_EXECUTIVE)
                .build();

        staff = VendorManager.builder()
                .email(new Email("staff"))
                .name("staff")
                .phone(new Phone("01012345679"))
                .password(new Password("password"))
                .id(3L)
                .role(Role.ROLE_VENDOR_STAFF)
                .build();

        vendor.addManager(executive);
        vendor.addManager(staff);
    }

    @Test
    void approveStaff_fail_NotExecutive() throws Exception {
        VendorManager staff2 = VendorManager.builder()
                .email(new Email("staff2"))
                .name("staff")
                .phone(new Phone("01012345671"))
                .password(new Password("password"))
                .id(4L)
                .role(Role.ROLE_VENDOR_STAFF)
                .build();

        VendorManagerAuthorityException exception =
                assertThrows(VendorManagerAuthorityException.class, () -> staff2.approve(staff));

        assertErrorCode(exception, ErrorCode.REQUIRE_MORE_THEN_EXECUTIVE_ROLE);
    }

    private void assertErrorCode(VendorManagerAuthorityException exception, ErrorCode errorCode) {
        assertThat(exception.getErrorCode()).isEqualTo(errorCode);
    }

    @Test
    void approveStaff_fail_NotApprovedManager() throws Exception {
        VendorManager executive2 = VendorManager.builder()
                .email(new Email("executive2"))
                .name("executive2")
                .phone(new Phone("01012345673"))
                .password(new Password("password"))
                .id(4L)
                .role(Role.ROLE_VENDOR_EXECUTIVE)
                .build();

        VendorManagerAuthorityException exception =
                assertThrows(VendorManagerAuthorityException.class, () -> executive2.approve(staff));

        assertErrorCode(exception, ErrorCode.NOT_APPROVED_VENDOR_MANAGER);
    }

    @Test
    void approveStaff_fail_NotSameVendor() throws Exception {
        Vendor other = Vendor.builder()
                .name("other")
                .regNumber("2")
                .ceo("other")
                .location(new Address("zipcode", "base", "detail"))
                .id(4L)
                .build();

        staff.setVendor(other);

        VendorManagerAuthorityException exception =
                assertThrows(VendorManagerAuthorityException.class, () -> executive.approve(staff));

        assertErrorCode(exception, ErrorCode.NOT_SAME_VENDOR);
    }

    @Test
    void approveStaff_success() throws Exception {
        executive.approve(staff);

        assertThat(staff.isApproved()).isTrue();
        assertThat(staff.getApprovalManager()).isEqualTo(executive);
    }
}