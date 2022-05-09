package xyz.fm.storerestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;

import java.util.Optional;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    boolean existsByEmail(Email email);
    Optional<Consumer> findByPhone(Phone phone);
}
