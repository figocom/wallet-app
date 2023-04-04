package com.softex.figo.walletapp.repository;

import com.softex.figo.walletapp.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByCcy(String ccy);
}
