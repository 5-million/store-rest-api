package xyz.fm.storerestapi.repository.user.vendor;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.repository.user.UserRepository;

import java.util.Optional;

public interface VendorManagerRepository extends UserRepository<VendorManager>, JpaRepository<VendorManager, Long> {


    @Override
    @EntityGraph(attributePaths = {"vendor"})
    Optional<VendorManager> findByEmail(String email);
}
