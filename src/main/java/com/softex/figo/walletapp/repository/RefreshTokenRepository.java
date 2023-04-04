package com.softex.figo.walletapp.repository;


import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.domain.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
@Transactional
public interface RefreshTokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    @Modifying
    void deleteByUser(AuthUser user);


}
