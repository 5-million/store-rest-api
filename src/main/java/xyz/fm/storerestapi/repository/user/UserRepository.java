package xyz.fm.storerestapi.repository.user;

import xyz.fm.storerestapi.entity.user.BaseUserEntity;

import java.util.Optional;

public interface UserRepository <T extends BaseUserEntity> {

    Optional<T> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
