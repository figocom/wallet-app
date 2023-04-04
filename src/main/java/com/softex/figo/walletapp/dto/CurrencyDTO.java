package com.softex.figo.walletapp.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
public class CurrencyDTO implements Serializable {
    private String code;
    private String ccy;
    private String ccyNm_RU;
    private String ccyNm_UZ;
    private String ccyNm_UZC;
    private String ccyNm_EN;
    private String nominal;
}
