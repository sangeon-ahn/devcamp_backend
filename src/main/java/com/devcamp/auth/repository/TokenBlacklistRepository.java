package com.devcamp.auth.repository;

import com.devcamp.auth.entity.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlackList, Long> {
    Optional<TokenBlackList> findByJti(String jti);

    List<TokenBlackList> findAllByExpiresAtLessThan(Date date);
}
