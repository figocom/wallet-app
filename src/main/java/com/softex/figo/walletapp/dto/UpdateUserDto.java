package com.softex.figo.walletapp.dto;

public record UpdateUserDto(
        String password,
        String confirmPassword
) {
}
