package xyz.fm.storerestapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.fm.storerestapi.entity.Category;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired private EntityManager em;
    @Autowired private CategoryRepository categoryRepository;

    @Test
    void existsByParentAndName_willReturn_true() throws Exception {
        Category parent = Category.builder().name("parent").build();
        Category presentCategory = Category.builder().parent(parent).name("child").build();

        em.persist(parent);
        em.persist(presentCategory);
        em.flush();
        em.clear();

        assertTrue(categoryRepository.existsByParentAndName(parent, "child"));
    }

    @Test
    void existsByParentAndName_willReturn_false() throws Exception {
        Category parent = Category.builder().name("parent").build();
        Category presentCategory = Category.builder().parent(parent).name("child").build();

        em.persist(parent);
        em.persist(presentCategory);
        em.flush();
        em.clear();

        assertFalse(categoryRepository.existsByParentAndName(parent, "child1"));
    }
}