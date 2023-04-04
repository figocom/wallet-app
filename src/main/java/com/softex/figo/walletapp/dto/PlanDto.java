package com.softex.figo.walletapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PlanDto {
    @NotBlank
    private String amount;
    private String planType;
    private Long categoryId;
}
