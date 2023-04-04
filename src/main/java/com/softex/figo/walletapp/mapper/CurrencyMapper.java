package com.softex.figo.walletapp.mapper;

import com.softex.figo.walletapp.domain.Currency;
import com.softex.figo.walletapp.dto.CurrencyDTO;
import lombok.NonNull;

import java.util.List;

public class CurrencyMapper implements BaseMapper<Currency, CurrencyDTO, CurrencyDTO, CurrencyDTO> {

    @Override
    public Currency fromCreateDTO(CurrencyDTO dto) {
        Currency currency = new Currency();
        currency.setCode(dto.getCode());
        currency.setCcy(dto.getCcy());
        currency.setCcyNm_EN(dto.getCcyNm_EN());
        currency.setCcyNm_RU(dto.getCcyNm_RU());
        currency.setCcyNm_UZ(dto.getCcyNm_UZ());
        currency.setCcyNm_UZC(dto.getCcyNm_UZC());
        currency.setNominal(dto.getNominal());
        return currency;
    }
    public List<Currency> fromCreateDTOs(List<CurrencyDTO> dtos) {
        return dtos.stream().map(this::fromCreateDTO).toList();
    }

    @Override
    public Currency fromUpdateDTO(CurrencyDTO dto) {
        return null;
    }

    @Override
    public CurrencyDTO toDTO(Currency domain) {
        return null;
    }

    @Override
    public List<CurrencyDTO> toDTOs(@NonNull List<Currency> domain) {
        return null;
    }


}



