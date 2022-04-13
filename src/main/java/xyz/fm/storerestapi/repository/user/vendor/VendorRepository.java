package xyz.fm.storerestapi.repository.user.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    boolean existsByRegistrationNumber(String registrationNumber);
}
