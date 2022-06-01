package xyz.fm.storerestapi.entity.user.vendor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.entity.Address;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
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
        vendor = new Vendor.Builder(
                "vendor",
                "1",
                "ceo",
                new Address("zipcode", "base", "detail")
        ).id(1L).build();

        executive = new VendorManager.Builder(
                new Email("executive"),
                "executive",
                new Phone("01012345678"),
                new Password("password")
        ).id(2L).approved(true).buildExecutive();

        staff = new VendorManager.Builder(
                new Email("staff"),
                "staff",
                new Phone("01012345679"),
                new Password("password")
        ).id(3L).buildStaff();

        vendor.addManager(executive);
        vendor.addManager(staff);
    }

    @Test
    void approveStaff_fail_NotExecutive() throws Exception {
        VendorManager staff2 = new VendorManager.Builder(
                new Email("staff2"),
                "staff",
                new Phone("01012345671"),
                new Password("password")
        ).id(4L).buildStaff();

        VendorManagerAuthorityException exception =
                assertThrows(VendorManagerAuthorityException.class, () -> staff2.approve(staff));

        assertErrorCode(exception, ErrorCode.REQUIRE_MORE_THEN_EXECUTIVE_ROLE);
    }

    private void assertErrorCode(VendorManagerAuthorityException exception, ErrorCode errorCode) {
        assertThat(exception.getErrorCode()).isEqualTo(errorCode);
    }

    @Test
    void approveStaff_fail_NotApprovedManager() throws Exception {
        VendorManager executive2 = new VendorManager.Builder(
                new Email("executive2"),
                "executive2",
                new Phone("01012345673"),
                new Password("password")
        ).id(4L).buildExecutive();

        VendorManagerAuthorityException exception =
                assertThrows(VendorManagerAuthorityException.class, () -> executive2.approve(staff));

        assertErrorCode(exception, ErrorCode.NOT_APPROVED_VENDOR_MANAGER);
    }

    @Test
    void approveStaff_fail_NotSameVendor() throws Exception {
        Vendor other = new Vendor.Builder(
                "other",
                "2",
                "other",
                new Address("zipcode", "base", "detail")
        ).id(4L).build();

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