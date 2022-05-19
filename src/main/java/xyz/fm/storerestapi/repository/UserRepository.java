package xyz.fm.storerestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.User;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class UserRepository {

    private final EntityManager em;

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public Optional<Consumer> findConsumerByEmail(String email) {
        return em.createQuery("select c from Consumer c where c.email = :email")
                .setParameter("email", new Email(email))
                .getResultList()
                .stream()
                .findFirst();

    }
}
