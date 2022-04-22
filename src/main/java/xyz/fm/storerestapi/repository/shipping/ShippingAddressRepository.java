package xyz.fm.storerestapi.repository.shipping;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;

import java.util.Optional;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {

    @Override
    @EntityGraph(attributePaths = {"consumer"})
    Optional<ShippingAddress> findById(Long shippingAddressId);
}
