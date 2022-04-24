package xyz.fm.storerestapi.repository.shipping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressInfo;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;

import java.util.List;
import java.util.Optional;

public interface ShippingAddressApiRepository extends JpaRepository<ShippingAddress, Long> {

    @Query("select new xyz.fm.storerestapi.dto.shipping.ShippingAddressInfo(c.id, sa.id, sa.address.zipcode, sa.address.address, sa.address.detailedAddress, sa.defaultAddress) " +
            "from ShippingAddress sa " +
            "left join sa.consumer c " +
            "where sa.id = :shippingAddressId and c.email = :consumerEmail")
    Optional<ShippingAddressInfo> findByIdAndConsumerEmail(@Param("shippingAddressId") Long shippingAddressId, @Param("consumerEmail") String consumerEmail);

    @Query("select new xyz.fm.storerestapi.dto.shipping.ShippingAddressInfo(c.id, sa.id, sa.address.zipcode, sa.address.address, sa.address.detailedAddress, sa.defaultAddress) " +
            "from ShippingAddress sa " +
            "left join sa.consumer c " +
            "where c.email = :consumerEmail")
    List<ShippingAddressInfo> findByConsumerEmail(@Param("consumerEmail") String consumerEmail);
}
