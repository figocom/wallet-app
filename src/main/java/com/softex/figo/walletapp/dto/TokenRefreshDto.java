package com.softex.figo.walletapp.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class TokenRefreshDto {
  @NotBlank(message = "Refresh token is required")
  private String refreshToken;
}
