package xyz.fm.storerestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    boolean existsByName(String name);
    boolean existsByRegNumber(String regNumber);
}
