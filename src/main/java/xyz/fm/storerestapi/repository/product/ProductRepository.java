package xyz.fm.storerestapi.repository.product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.product.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findById(Long id);

    @EntityGraph(attributePaths = {"category", "items", "items.vendorItemList", "items.vendorItemList.vendor"})
    Optional<Product> findAllFetchedById(Long id);
}
