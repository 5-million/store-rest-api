package xyz.fm.storerestapi.repository.user.vendor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerInfo;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VendorManagerApiRepositoryTest {

    @Autowired protected EntityManager em;
    @Autowired protected VendorManagerApiRepository vendorManagerApiRepository;

    @Test
    @DisplayName("findByVendor success")
    public void findByVendor_success() throws Exception {
        //given
        Vendor vendor = new Vendor.Builder("a", "a", "010", "a").build();
        VendorManager a = new VendorManager.Builder("aa@a.a", "aa", "aa", "01012345671", vendor).build();
        VendorManager b = new VendorManager.Builder("ab@a.a", "ab", "aa", "01012345672", vendor).build();
        VendorManager c = new VendorManager.Builder("ac@a.a", "ac", "aa", "01012345673", vendor).build();
        VendorManager d = new VendorManager.Builder("ad@a.a", "ad", "aa", "01012345674", vendor).build();
        VendorManager e = new VendorManager.Builder("af@a.a", "af", "aa", "01012345675", vendor).build();

        vendor.addManager(a);
        vendor.addManager(b);
        vendor.addManager(c);
        vendor.addManager(d);
        vendor.addManager(e);

        em.persist(vendor);
        em.flush();
        em.clear();

        //when
        List<VendorManagerInfo> managers = vendorManagerApiRepository.findByVendor(vendor);

        //then
        assertThat(managers.size()).isEqualTo(5);
    }
}