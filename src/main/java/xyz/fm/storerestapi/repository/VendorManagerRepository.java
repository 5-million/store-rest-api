package xyz.fm.storerestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;

import java.util.Optional;

public interface VendorManagerRepository extends JpaRepository<VendorManager, Long> {

    boolean existsByEmail(Email email);
    boolean existsByPhone(Phone phone);
    Optional<VendorManager> findByEmail(Email email);
    Optional<VendorManager> findByPhone(Phone phone);
}
