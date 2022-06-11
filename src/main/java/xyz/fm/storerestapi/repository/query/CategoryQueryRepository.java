package xyz.fm.storerestapi.repository.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.fm.storerestapi.dto.category.CategoryBriefInfo;
import xyz.fm.storerestapi.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryQueryRepository extends JpaRepository<Category, Long> {

    @Query("select new xyz.fm.storerestapi.dto.category.CategoryBriefInfo(c.id, c.name, c.depth, c.parent.id) " +
            "from Category c " +
            "where c.id = :categoryId"
    )
    Optional<CategoryBriefInfo> findBriefInfoById(@Param("categoryId") Long id);

    List<Category> findAllByDepthIs(Integer depth);
}
