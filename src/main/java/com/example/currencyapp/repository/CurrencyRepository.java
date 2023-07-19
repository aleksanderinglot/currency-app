package com.example.currencyapp.repository;

import com.example.currencyapp.model.CurrencyRequestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyRequestData, Long> {
}
