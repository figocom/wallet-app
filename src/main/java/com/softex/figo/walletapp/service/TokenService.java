package com.softex.figo.walletapp.service;

import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.domain.Token;
import com.softex.figo.walletapp.exception.TokenRefreshException;
import com.softex.figo.walletapp.repository.AuthUserRepository;
import com.softex.figo.walletapp.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthUserRepository userRepository;
    @Transactional
    public void deleteByUserName(String username) {
        Optional<AuthUser> byUsername = userRepository.findByUsername(username);
        byUsername.ifPresent(refreshTokenRepository::deleteByUser);
    }
    public Optional<Token> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    public Token verifyExpiration(Token token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public void save(Token token) {
        refreshTokenRepository.save(token);
    }
}
