package com.example.currencyapp.service;

import com.example.currencyapp.exception.CurrencyNotFoundException;
import com.example.currencyapp.exception.ExternalAPIException;
import com.example.currencyapp.model.CurrencyRequest;
import com.example.currencyapp.model.CurrencyRequestData;
import com.example.currencyapp.model.CurrencyValue;
import com.example.currencyapp.model.CurrencyValueResponse;
import com.example.currencyapp.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;

    public CurrencyRequestData getCurrentCurrencyValue(CurrencyRequest request) {
        String currencyCode = request.getCurrency();
        String name = request.getName();

        try {
            String apiUrl = "http://api.nbp.pl/api/exchangerates/tables/A?format=json";
            ResponseEntity<CurrencyValueResponse[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, CurrencyValueResponse[].class);
            CurrencyValueResponse[] responses = response.getBody();

            if (responses != null && responses.length > 0) {
                CurrencyValueResponse currencyValueResponse = responses[0];
                List<CurrencyValue> currencyValues = currencyValueResponse.getRates();

                if (currencyValues != null) {
                    for (CurrencyValue currencyValue : currencyValues) {
                        if (currencyValue.getCode() != null && currencyValue.getCode().equals(currencyCode)) {
                            CurrencyRequestData requestData = new CurrencyRequestData();
                            requestData.setCurrency(currencyValue.getCurrency());
                            requestData.setName(name);
                            requestData.setDate(LocalDateTime.now());
                            requestData.setCurrencyValue(currencyValue.getMid());
                            currencyRepository.save(requestData);

                            return requestData;
                        }
                    }
                }
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CurrencyNotFoundException("Currency not found");
            } else {
                throw new ExternalAPIException("External API error");
            }
        }

        throw new CurrencyNotFoundException("Currency not found");
    }


    public List<CurrencyRequestData> getAllCurrencyRequests() {
        return currencyRepository.findAll();
    }
}
