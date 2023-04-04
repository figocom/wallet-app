package com.softex.figo.walletapp.repository;

import com.softex.figo.walletapp.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    @Query("SELECT u FROM AuthUser u WHERE u.username = :username")
    Optional<AuthUser> findByUsername(String username);
}