package com.softex.figo.walletapp.controller;

import com.softex.figo.walletapp.config.security.AuthUserDetails;
import com.softex.figo.walletapp.config.security.JwtProvider;
import com.softex.figo.walletapp.domain.Token;
import com.softex.figo.walletapp.dto.LoginDto;
import com.softex.figo.walletapp.dto.RegisterDto;
import com.softex.figo.walletapp.dto.TokenRefreshDto;
import com.softex.figo.walletapp.exception.TokenRefreshException;
import com.softex.figo.walletapp.response.DataDTO;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.TokenRefreshResponse;
import com.softex.figo.walletapp.response.WebResponse;
import com.softex.figo.walletapp.service.AuthService;
import com.softex.figo.walletapp.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;


    @PostMapping("/login")
    public ResponseEntity<WebResponse<?>> login(@Valid @RequestBody LoginDto loginDto) {
        WebResponse<?> login = authService.login(loginDto);
        return ResponseEntity.status((login.data() instanceof ErrorDTO errorDTO) ? errorDTO.getError_code() : 200).body(login);
    }

    @PostMapping("/register")
    public ResponseEntity<WebResponse<?>> register(@Valid @RequestBody RegisterDto registerDto) {
        WebResponse<?> register = authService.register(registerDto);
        return ResponseEntity.status((register.data() instanceof ErrorDTO errorDTO) ? errorDTO.getError_code() : 201).body(register);
    }
    @GetMapping("/register")
    public ResponseEntity<WebResponse<?>> register() {
        WebResponse<?> currencies=authService.getCurrencies();
        return ResponseEntity.status(200).body(currencies);
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<WebResponse<TokenRefreshResponse>> refreshToken(@Valid @RequestBody TokenRefreshDto request) {
        AuthUserDetails aud= (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String requestRefreshToken = request.getRefreshToken();
        return tokenService.findByToken(requestRefreshToken)
                .map(tokenService::verifyExpiration)
                .map(Token::getUser)
                .map(user -> {
                    String token = jwtProvider.generateToken(Map.of(),aud.authUser(),false);
                    TokenRefreshResponse tokenRefreshResponse = TokenRefreshResponse.builder().accessToken(token).refreshToken(requestRefreshToken).build();
                    return ResponseEntity.ok(new WebResponse<>(tokenRefreshResponse));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<WebResponse<?>> logoutUser() {
        AuthUserDetails aud= (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        tokenService.deleteByUserName(aud.getUsername());
        return ResponseEntity.ok(new WebResponse<>(new DataDTO<>("Log out successfully")));
    }


}
