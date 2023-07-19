package com.example.currencyapp.model;

import lombok.Data;

@Data
public class CurrencyValue {
    private String currency;
    private String code;
    private double mid;
}