package com.softex.figo.walletapp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class ConvertCurrencyDto implements Serializable {
    private String id;
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Ccy")
    private String ccy;
    @JsonProperty("CcyNm_RU")
    private String ccyNm_RU;
    @JsonProperty("CcyNm_UZ")
    private String ccyNm_UZ;
    @JsonProperty("CcyNm_UZC")
    private String ccyNm_UZC;
    @JsonProperty("CcyNm_EN")
    private String ccyNm_EN;
    @JsonProperty("Nominal")
    private String nominal;
    @JsonProperty("Rate")
    private String rate;
    @JsonProperty("Diff")
    private String diff;
    @JsonProperty("Date")
    private String date;
}
