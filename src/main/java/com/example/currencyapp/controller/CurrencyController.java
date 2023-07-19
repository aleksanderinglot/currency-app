package com.example.currencyapp.controller;

import com.example.currencyapp.model.CurrencyRequest;
import com.example.currencyapp.model.CurrencyRequestData;
import com.example.currencyapp.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currencies")
@CrossOrigin(origins = "http://localhost:4200")
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping("/get-current-currency-value-command")
    public ResponseEntity<CurrencyRequestData> getCurrentCurrencyValue(@RequestBody CurrencyRequest request) {
        CurrencyRequestData requestData = currencyService.getCurrentCurrencyValue(request);
        return ResponseEntity.ok(requestData);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<CurrencyRequestData>> getAllCurrencyRequests() {
        List<CurrencyRequestData> requestList = currencyService.getAllCurrencyRequests();
        return ResponseEntity.ok(requestList);
    }
}
