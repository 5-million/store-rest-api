package xyz.fm.storerestapi.repository.user.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerInfo;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;

import java.util.List;

public interface VendorManagerApiRepository extends JpaRepository<VendorManager, Long> {

    List<VendorManagerInfo> findByVendor(Vendor vendor);
}
