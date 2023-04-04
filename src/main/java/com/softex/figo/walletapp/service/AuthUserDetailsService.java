package com.softex.figo.walletapp.service;

import com.softex.figo.walletapp.config.security.AuthUserDetails;
import com.softex.figo.walletapp.config.security.JwtProvider;
import com.softex.figo.walletapp.dto.LoginDto;
import com.softex.figo.walletapp.dto.TokenRefreshDto;
import com.softex.figo.walletapp.repository.AuthUserRepository;
import com.softex.figo.walletapp.response.TokenRefreshResponse;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ComponentScan("com.softex.figo.walletapp.repository")
public class AuthUserDetailsService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;


    public AuthUserDetailsService(AuthUserRepository authUserRepository, @Lazy AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authUserRepository = authUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthUserDetails(authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    public TokenRefreshResponse generateToken(@NonNull LoginDto dto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (!authenticate.isAuthenticated())
            throw new BadCredentialsException("Bad credentials");
        String accessToken = jwtProvider.generateToken(Map.of(), loadUserByUsername(dto.getUsername()).authUser(), false);
        String refreshToken = jwtProvider.generateToken(loadUserByUsername(dto.getUsername()).authUser());
        return TokenRefreshResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

}
