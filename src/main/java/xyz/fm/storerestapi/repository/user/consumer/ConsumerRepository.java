package xyz.fm.storerestapi.repository.user.consumer;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.repository.user.UserRepository;

public interface ConsumerRepository extends UserRepository<Consumer>, JpaRepository<Consumer, Long> {
}
