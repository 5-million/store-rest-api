package xyz.fm.storerestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.fm.storerestapi.entity.Jwt;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<Jwt, Long> {

    Optional<Jwt> findByEmail(String email);
}
