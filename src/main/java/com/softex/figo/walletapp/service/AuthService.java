package com.softex.figo.walletapp.service;

import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.domain.Currency;
import com.softex.figo.walletapp.dto.LoginDto;
import com.softex.figo.walletapp.dto.RegisterDto;
import com.softex.figo.walletapp.exception.ItemNotFoundException;
import com.softex.figo.walletapp.mapper.UserMapper;
import com.softex.figo.walletapp.repository.AuthUserRepository;
import com.softex.figo.walletapp.repository.CurrencyRepository;
import com.softex.figo.walletapp.response.DataDTO;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.TokenRefreshResponse;
import com.softex.figo.walletapp.response.WebResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthUserRepository authUserRepository;
    private final UserMapper userMapper;
    private final AuthUserDetailsService authUserDetailsService;
    private final CurrencyRepository currencyRepository;


    public WebResponse<?> login(LoginDto loginDto) {
        try {
            TokenRefreshResponse response = authUserDetailsService.generateToken(loginDto);
            return new WebResponse<>(new DataDTO<>(response));
        } catch (BadCredentialsException badCredentialsException) {
            return new WebResponse<>(new ErrorDTO(badCredentialsException, 400));
        }
    }

    public WebResponse<?> register(RegisterDto registerDto) {
        try {
            if(!StringUtils.isNumeric(registerDto.getCurrency_id())){return new WebResponse<>(new ErrorDTO("Currency id is not numeric", 400));}
            if (Objects.isNull(registerDto.getConfirmPassword()) || !registerDto.getConfirmPassword().equals(registerDto.getPassword()))
                return new WebResponse<>(new ErrorDTO("Confirm password is  not equals to password", 400));
            AuthUser entity = userMapper.fromCreateDTO(registerDto);
            if (Objects.nonNull(registerDto.getUsername())) {
                Optional<AuthUser> byUsername = authUserRepository.findByUsername(entity.getUsername());
                if (byUsername.isEmpty()) {
                    authUserRepository.save(entity);
                    TokenRefreshResponse response = authUserDetailsService.generateToken(new LoginDto(registerDto.getUsername(), registerDto.getPassword()));
                    return new WebResponse<>(new DataDTO<>(response));
                } else return new WebResponse<>(new ErrorDTO("User already exist", 409));
            } else return new WebResponse<>(new ErrorDTO("Username is null", 400));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public WebResponse<?> getCurrencies() {
        List<Currency> all = currencyRepository.findAll();
        return new WebResponse<>(new DataDTO<>(all));
    }
}
