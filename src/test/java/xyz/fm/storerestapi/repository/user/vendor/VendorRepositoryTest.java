package xyz.fm.storerestapi.repository.user.vendor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.fm.storerestapi.dto.user.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.util.EncryptUtil;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class VendorRepositoryTest {

    @Autowired protected EntityManager em;
    @Autowired protected VendorRepository vendorRepository;

    @Test
    @DisplayName("addManager success")
    public void addManager_success() throws Exception {
        //given
        Vendor vendor = new Vendor.Builder("apple", "cook", "010", "usa").build();
        vendorRepository.save(vendor);
        em.flush();
        em.clear();

        //when
        Vendor findVendor = vendorRepository.findById(vendor.getId()).get();
        VendorManager manager = new VendorManager.Builder("email", "name", "pwdq", "01012345678", findVendor).build();
        findVendor.addManager(manager);
        em.flush();
        em.clear();

        //then
        assertThat(manager.getId()).isNotNull();
        VendorManager findManager = em.find(VendorManager.class, manager.getId());
        assertThat(findManager.getId()).isEqualTo(manager.getId());
        assertThat(findManager.getVendor().getId()).isEqualTo(vendor.getId());
    }

    @Test
    @DisplayName("register success")
    public void register_success() throws Exception {
        //given
        VendorRegisterRequest request = new VendorRegisterRequest(
                "apple", "cook", "101", "usa",
                new VendorRegisterRequest.VendorAdmin(
                        "admin@test.com",
                        "admin",
                        "pwd",
                        "pwd",
                        "01012345678"
                )
        );

        Vendor vendor = new Vendor.Builder(
                request.getVendorName(),
                request.getCeo(),
                request.getRegistrationNumber(),
                request.getLocation()
        ).build();

        VendorManager admin = new VendorManager.Builder(
                request.getAdmin().getEmail(),
                request.getAdmin().getName(),
                EncryptUtil.encode(request.getAdmin().getPassword()),
                request.getAdmin().getPhoneNumber(),
                vendor
        ).build();

        vendor.addManager(admin);

        //when
        vendorRepository.save(vendor);
        em.flush();
        em.clear();

        //then
        Vendor resultVendor = vendorRepository.findById(vendor.getId()).get();
        VendorManager resultAdmin = em.find(VendorManager.class, admin.getId());

        assertThat(resultVendor.getManagers().size()).isEqualTo(1);
        assertThat(resultAdmin.getVendor().getId()).isEqualTo(resultVendor.getId());
        assertThat(resultAdmin.getId()).isEqualTo(resultVendor.getManagers().get(0).getId());
    }
}
