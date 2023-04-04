package com.softex.figo.walletapp.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TokenRefreshResponse implements MResponse{
  private String accessToken;
  private String refreshToken;
  @Builder.Default
  private String tokenType = "Bearer";

}
