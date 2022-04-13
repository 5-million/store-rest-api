package xyz.fm.storerestapi.entity.user.category;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.fm.storerestapi.entity.category.Category;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CategoryBusinessTest {

    @Autowired protected EntityManager em;

    @Test
    public void addChild() {
        //given
        Category laptop = new Category.Builder("laptop").build();
        em.persist(laptop);

        //when
        Category apple = new Category.Builder("apple").build();
        laptop.addChild(apple);
        em.persist(apple);
        em.flush();
        em.clear();

        //then
        apple = em.find(Category.class, apple.getId());
        assertThat(apple.getParent().getId()).isEqualTo(laptop.getId());
        assertThat(apple.getDepth()).isEqualTo(apple.getParent().getDepth() + 1);
    }
}
