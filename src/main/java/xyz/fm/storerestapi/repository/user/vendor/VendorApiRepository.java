package xyz.fm.storerestapi.repository.user.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.dto.user.vendor.VendorInfo;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;

import java.util.List;

public interface VendorApiRepository extends JpaRepository<Vendor, Long> {

    List<VendorInfo> findAllVendorInfoBy();
}
