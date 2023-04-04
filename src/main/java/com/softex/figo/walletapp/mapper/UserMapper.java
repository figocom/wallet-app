package com.softex.figo.walletapp.mapper;


import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.domain.Currency;
import com.softex.figo.walletapp.dto.RegisterDto;
import com.softex.figo.walletapp.dto.UpdateUserDto;
import com.softex.figo.walletapp.exception.ItemNotFoundException;
import com.softex.figo.walletapp.repository.CurrencyRepository;
import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserMapper implements BaseMapper<AuthUser, RegisterDto, RegisterDto, UpdateUserDto> {
    private final CurrencyRepository currencyRepository;
    private final BCryptPasswordEncoder encoder;

    public UserMapper(CurrencyRepository currencyRepository, @Lazy BCryptPasswordEncoder encoder) {
        this.currencyRepository = currencyRepository;
        this.encoder = encoder;
    }

    @Override
    public AuthUser fromCreateDTO(@NonNull RegisterDto dto) throws ItemNotFoundException {
        try {
            if (Objects.nonNull(dto.getCurrency_id())) {
                Optional<Currency> repositoryById = currencyRepository.findById(Long.valueOf(dto.getCurrency_id()));
                Double balance = Double.valueOf(dto.getBalance());
                if (repositoryById.isPresent()) {
                    return AuthUser.builder().username(dto.getUsername()).
                            password(encoder.encode(dto.getPassword()))
                            .name(dto.getName()).
                            balance(balance).
                            currency(repositoryById.get()).
                            build();
                }
                else throw new ItemNotFoundException("Currency not found");
            } else throw new ItemNotFoundException("Currency id is null");

        } catch (Exception e) {
            throw new ItemNotFoundException("Some fields  not found");
        }
    }

    @Override
    public AuthUser fromUpdateDTO(@NonNull UpdateUserDto dto) {
        return null;
    }

    @Override
    public RegisterDto toDTO(@NonNull AuthUser domain) {
        return null;
    }

    @Override
    public List<RegisterDto> toDTOs(@NonNull List<AuthUser> domain) {
        return null;
    }
}
