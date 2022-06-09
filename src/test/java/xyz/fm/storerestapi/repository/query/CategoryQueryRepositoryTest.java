package xyz.fm.storerestapi.repository.query;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.fm.storerestapi.dto.category.CategoryBriefInfo;
import xyz.fm.storerestapi.entity.Category;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryQueryRepositoryTest {

    @Autowired private EntityManager em;
    @Autowired private CategoryQueryRepository categoryQueryRepository;

    Category parent, child;

    @BeforeEach
    void beforeEach() {
        parent = Category.builder().name("parent").build();
        child = Category.builder().name("child").parent(parent).build();

        categoryQueryRepository.save(parent);
        categoryQueryRepository.save(child);

        em.flush();
        em.clear();
    }

    @Test
    void findBriefInfoById() throws Exception {
        Optional<CategoryBriefInfo> result = categoryQueryRepository.findBriefInfoById(child.getId());
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findBriefInfoById_categoryIdIsNull() throws Exception {
        Optional<CategoryBriefInfo> result = categoryQueryRepository.findBriefInfoById(null);
        assertThat(result.isEmpty()).isTrue();
    }
}