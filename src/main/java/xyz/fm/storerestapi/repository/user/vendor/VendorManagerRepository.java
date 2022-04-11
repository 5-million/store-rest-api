package xyz.fm.storerestapi.repository.user.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.repository.user.UserRepository;

public interface VendorManagerRepository extends UserRepository<VendorManager>, JpaRepository<VendorManager, Long> {
}
