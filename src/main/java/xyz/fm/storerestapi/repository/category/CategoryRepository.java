package xyz.fm.storerestapi.repository.category;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xyz.fm.storerestapi.entity.category.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    @EntityGraph(attributePaths = {"parent"})
    Optional<Category> findById(Long id);

    boolean existsByCategoryName(String categoryName);

    @Query("select c from Category c order by c.depth")
    List<Category> findAllSortByDepth();
}
