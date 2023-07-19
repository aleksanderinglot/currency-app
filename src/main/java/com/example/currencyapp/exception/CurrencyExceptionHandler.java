package com.example.currencyapp.exception;

import com.example.currencyapp.model.CurrencyErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CurrencyExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CurrencyErrorResponse> handleCurrencyNotFoundException(CurrencyNotFoundException ex) {
        CurrencyErrorResponse currencyErrorResponse = new CurrencyErrorResponse();

        currencyErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        currencyErrorResponse.setMessage(ex.getMessage());
        currencyErrorResponse.setTimeStamp(System.currentTimeMillis());

        return ResponseEntity.status(404).body(currencyErrorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<CurrencyErrorResponse> handleExternalAPIException(ExternalAPIException ex) {
        CurrencyErrorResponse currencyErrorResponse = new CurrencyErrorResponse();

        currencyErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        currencyErrorResponse.setMessage(ex.getMessage());
        currencyErrorResponse.setTimeStamp(System.currentTimeMillis());

        return ResponseEntity.status(500).body(currencyErrorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<CurrencyErrorResponse> handleException(Exception ex) {
        CurrencyErrorResponse currencyErrorResponse = new CurrencyErrorResponse();

        currencyErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        currencyErrorResponse.setMessage(ex.getMessage());
        currencyErrorResponse.setTimeStamp(System.currentTimeMillis());

        return ResponseEntity.status(400).body(currencyErrorResponse);
    }
}
