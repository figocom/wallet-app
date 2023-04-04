package com.softex.figo.walletapp.dto;

public record MoneyCirculationDto(
        String note,
        Double amount,
        Long categoryId,
        Long subCategoryId
) {
}
