package xyz.fm.storerestapi.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.Role;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VendorTest {

    @Autowired private EntityManager em;
    private Address location;
    private Vendor vendor;

    @BeforeEach
    void beforeEach() {
        location = new Address("zipcode", "base address", "detail address");
        vendor = buildVendor("store", "1", "kim", location);
    }

    @Test
    void saveTest() {
        //when
        em.persist(vendor);

        //then
        emFlushAndClear();
        Vendor foundVendor = em.find(Vendor.class, vendor.getId());

        assertThat(foundVendor.getName()).isEqualTo(vendor.getName());
        assertThat(foundVendor.getRegNumber()).isEqualTo(vendor.getRegNumber());
        assertThat(foundVendor.getCeo()).isEqualTo(vendor.getCeo());
        assertThat(foundVendor.getLocation().equals(vendor.getLocation())).isTrue();
    }


    @Test
    void save_fail_duplicateVendorName() {
        //given
        Vendor duplicatedNameVendor = buildVendor(vendor.getName(), "2", "park", location);

        //when
        em.persist(vendor);
        em.persist(duplicatedNameVendor);

        //then
        assertPersistenceException(this::emFlushAndClear);
    }

    @Test
    void save_fail_duplicateVendorRegNumber() {
        //given
        Vendor duplicatedRegNumberVendor = buildVendor("a", vendor.getRegNumber(), "jo", location);

        //when
        em.persist(vendor);
        em.persist(duplicatedRegNumberVendor);

        //then
        assertPersistenceException(this::emFlushAndClear);
    }

    @Test
    void addManager() {
        VendorManager executive = VendorManager.builder()
                .email(new Email("vendor@vendor.com"))
                .name("executive")
                .phone(new Phone("01012345678"))
                .password(new Password("password"))
                .vendor(vendor)
                .approved(true)
                .role(Role.ROLE_VENDOR_EXECUTIVE)
                .build();

        vendor.addManager(executive);

        em.persist(vendor);
        em.flush();
        em.clear();

        assertNotNull(executive.getId());
        VendorManager foundManager = em.find(VendorManager.class, executive.getId());
        assertThat(vendor.getVendorManagerList().size()).isEqualTo(1);
        assertThat(foundManager.getVendor().getId()).isEqualTo(vendor.getId());
    }

    private Vendor buildVendor(String name, String regNumber, String ceo, Address location) {
        return Vendor.builder()
                .name(name)
                .regNumber(regNumber)
                .ceo(ceo)
                .location(location)
                .build();
    }

    private void emFlushAndClear() {
        em.flush();
        em.clear();
    }

    private void assertPersistenceException(Executable executable) {
        assertThrows(PersistenceException.class, executable);
    }
}