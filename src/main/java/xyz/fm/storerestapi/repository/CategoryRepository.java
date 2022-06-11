package xyz.fm.storerestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByParentAndName(Category parent, String name);
}
