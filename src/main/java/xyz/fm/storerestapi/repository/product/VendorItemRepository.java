package xyz.fm.storerestapi.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.item.VendorItem;

public interface VendorItemRepository extends JpaRepository<VendorItem, Long> {
}
