package xyz.fm.storerestapi.repository.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.fm.storerestapi.dto.vendor.VendorManagerInfo;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;

import java.util.List;

public interface VendorManagerQueryRepository extends JpaRepository<VendorManager, Long> {

    @Query("SELECT new xyz.fm.storerestapi.dto.vendor.VendorManagerInfo(" +
            "vm.id, vm.vendor.id, vm.email.email, vm.name, vm.phone.phone, vm.approved, vm.approvalManager.id, vm.role) " +
            "from VendorManager vm " +
            "where vm.vendor = :vendor")
    List<VendorManagerInfo> findAllByVendorId(@Param("vendor") Vendor vendor);
}
