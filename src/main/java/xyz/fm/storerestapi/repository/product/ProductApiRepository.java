package xyz.fm.storerestapi.repository.product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.fm.storerestapi.dto.product.SimpleProduct;
import xyz.fm.storerestapi.entity.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductApiRepository extends JpaRepository<Product, Long> {

    @Query(value = "select new xyz.fm.storerestapi.dto.product.SimpleProduct(c.id, c.categoryName, p.id, p.productName, i.id, i.selections, i.salesQuantity, v.id, vi.id, vi.price, vi.discountRate) " +
            "from Product p " +
            "left join p.category c " +
            "left join p.items i " +
            "left join i.vendorItemList vi " +
            "left join vi.vendor v " +
            "where c.id = :categoryId")
    List<SimpleProduct> findByCategory(@Param("categoryId") Long categoryId);
}
