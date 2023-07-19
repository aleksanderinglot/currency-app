package com.example.currencyapp.model;

import lombok.Data;

import java.util.List;

@Data
public class CurrencyValueResponse {
    private String table;
    private String no;
    private String effectiveDate;
    private List<CurrencyValue> rates;
}

