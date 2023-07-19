package com.example.currencyapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyErrorResponse {
    private int status;
    private String message;
    private long timeStamp;
}
