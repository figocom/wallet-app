package com.softex.figo.walletapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency extends Auditable{
    @NotBlank(message = "Code is mandatory")
    @Column(nullable = false, unique = true)
    private String code;
    @NotBlank(message = "Ccy is mandatory")
    @Column(nullable = false )
    private String ccy;
    @NotBlank(message = "Name ru is mandatory")
    @Column(nullable = false )
    private String CcyNm_RU;
    @NotBlank(message = "Name uz is mandatory")
    @Column(nullable = false )
    private String CcyNm_UZ;
    @NotBlank(message = "Name uzc is mandatory")
    @Column(nullable = false )
    private String CcyNm_UZC;
    @NotBlank(message = "Name english is mandatory")
    @Column(nullable = false )
    private String CcyNm_EN;
    @NotBlank(message = "Nominal is mandatory")
    @Column(nullable = false )
    private String Nominal;

}
