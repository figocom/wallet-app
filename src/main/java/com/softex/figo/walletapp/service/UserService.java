package com.softex.figo.walletapp.service;

import com.softex.figo.walletapp.config.security.AuthUserDetails;
import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.dto.UpdateUserDto;
import com.softex.figo.walletapp.repository.AuthUserRepository;
import com.softex.figo.walletapp.response.DataDTO;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service

public class UserService {
    private final AuthUserRepository authUserRepository;

    private final BCryptPasswordEncoder encoder;

    public UserService(AuthUserRepository authUserRepository, @Lazy BCryptPasswordEncoder encoder) {
        this.authUserRepository = authUserRepository;
        this.encoder = encoder;
    }

    public WebResponse<?> getBalance() {
        AuthUser currentUser = getCurrentUser();
        Optional<AuthUser> authUser = authUserRepository.findByUsername(currentUser.getUsername());
        if (authUser.isEmpty()) return new WebResponse<>(new ErrorDTO("User not found", 404));
        return new WebResponse<>(new DataDTO<>(authUser.get().getBalance()));
    }

    public AuthUser getCurrentUser() {
        AuthUserDetails userDetails = (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.authUser();
    }

    public WebResponse<?>editPassword(UpdateUserDto updateUserDto) {
        if (Objects.isNull(updateUserDto.password()) || updateUserDto.password().length() < 8)
            return new WebResponse<>(new ErrorDTO("Password is null or less than 8 characters", 400));
        if (Objects.isNull(updateUserDto.confirmPassword()) || !updateUserDto.confirmPassword().equals(updateUserDto.password()))
            return new WebResponse<>(new ErrorDTO("Confirm password is null or not equals to password", 400));
        AuthUser currentUser = getCurrentUser();
        if (!encoder.matches(updateUserDto.password(), currentUser.getPassword()))
            return new WebResponse<>(new ErrorDTO("Password is equal old password", 400));
        currentUser.setPassword(encoder.encode(updateUserDto.password()));
        authUserRepository.save(currentUser);
        return new WebResponse<>(new DataDTO<>("Password successfully changed"));
    }
}
