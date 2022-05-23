package xyz.fm.storerestapi.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        vendor = new Vendor.Builder(
                "store",
                "1",
                "kim",
                location
        ).build();
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

    private void emFlushAndClear() {
        em.flush();
        em.clear();
    }


    @Test
    void save_fail_duplicateVendorName() {
        //given
        Vendor duplicatedNameVendor = new Vendor.Builder(
                vendor.getName(),
                "2",
                "park",
                location
        ).build();

        //when
        em.persist(vendor);
        em.persist(duplicatedNameVendor);

        //then
        assertPersistenceException(this::emFlushAndClear);
    }

    private void assertPersistenceException(Executable executable) {
        assertThrows(PersistenceException.class, executable);
    }

    @Test
    void save_fail_duplicateVendorRegNumber() {
        //given
        Vendor duplicatedRegNumberVendor = new Vendor.Builder(
                "a",
                vendor.getRegNumber(),
                "jo",
                location
        ).build();

        //when
        em.persist(vendor);
        em.persist(duplicatedRegNumberVendor);

        //then
        assertPersistenceException(this::emFlushAndClear);
    }
}