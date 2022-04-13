package xyz.fm.storerestapi.repository.user.vendor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.fm.storerestapi.dto.user.vendor.VendorInfo;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class VendorApiRepositoryTest {

    @Autowired private EntityManager em;
    @Autowired private VendorApiRepository vendorApiRepository;
    
    @Test
    @DisplayName("findAllVendorInfoBy success")
    public void findAllVendorInfoBy_success() throws Exception {
        //given
        Vendor apple = new Vendor.Builder("apple", "cook", "010", "usa").build();
        Vendor samsung = new Vendor.Builder("samsung", "jaemyung", "011", "seoul").build();

        em.persist(apple);
        em.persist(samsung);
        em.flush();
        em.clear();
        
        //when
        List<VendorInfo> result = vendorApiRepository.findAllVendorInfoBy();

        //then
        assertThat(result.size()).isEqualTo(2);
    }
}