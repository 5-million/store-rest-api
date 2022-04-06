package xyz.fm.storerestapi.entity.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.fm.storerestapi.util.EncryptUtil;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ConsumerBusinessTest {

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("login fail: incorrect password")
    public void login_fail_incorrectPassword() {
        String plainTextPassword = "password";
        String encodedPassword = EncryptUtil.encode("pessword");

        Consumer consumer = new Consumer.Builder(
                "email",
                "name",
                encodedPassword,
                "01012345678",
                new AdsReceive(true, true, true)
        ).build();

        em.persist(consumer);
        em.flush();
        em.clear();

        assertFalse(consumer.login(plainTextPassword));
    }

    @Test
    @DisplayName("login success")
    public void login_success() {
        String plainTextPassword = "password";
        String encodedPassword = EncryptUtil.encode(plainTextPassword);

        Consumer consumer = new Consumer.Builder(
                "email",
                "name",
                encodedPassword,
                "01012345678",
                new AdsReceive(true, true, true)
        ).build();

        em.persist(consumer);
        em.flush();
        em.clear();

        assertNull(consumer.getLastLoginDate());
        assertTrue(consumer.login(plainTextPassword));
        em.flush();
        em.clear();

        assertNotNull(consumer.getLastLoginDate());
    }
}
