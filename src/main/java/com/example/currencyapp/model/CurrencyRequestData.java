package com.example.currencyapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CurrencyRequestData {

    @Id
    @GeneratedValue
    private Long id;
    private String currency;
    private String name;
    private LocalDateTime date;
    private double currencyValue;
}