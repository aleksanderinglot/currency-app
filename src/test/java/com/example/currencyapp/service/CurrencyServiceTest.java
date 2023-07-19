package com.example.currencyapp.service;

import com.example.currencyapp.exception.CurrencyNotFoundException;
import com.example.currencyapp.exception.ExternalAPIException;
import com.example.currencyapp.model.CurrencyRequest;
import com.example.currencyapp.model.CurrencyRequestData;
import com.example.currencyapp.model.CurrencyValue;
import com.example.currencyapp.model.CurrencyValueResponse;
import com.example.currencyapp.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetCurrentCurrencyValue() {
        CurrencyRequest request = new CurrencyRequest();
        request.setCurrency("EUR");
        request.setName("Jan Kowalski");

        CurrencyValueResponse response = new CurrencyValueResponse();
        CurrencyValue currencyValue = new CurrencyValue();
        currencyValue.setCode("EUR");
        currencyValue.setCurrency("euro");
        currencyValue.setMid(4.4331);
        response.setRates(List.of(currencyValue));

        ResponseEntity<CurrencyValueResponse[]> mockResponseEntity = ResponseEntity.ok(new CurrencyValueResponse[]{response});

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(CurrencyValueResponse[].class)))
                .thenReturn(mockResponseEntity);

        CurrencyRequestData requestData = currencyService.getCurrentCurrencyValue(request);

        assertEquals("euro", requestData.getCurrency());
        assertEquals("Jan Kowalski", requestData.getName());
        assertNotNull(requestData.getDate());
        assertEquals(4.4331, requestData.getCurrencyValue());
        verify(currencyRepository, times(1)).save(requestData);
    }

    @Test
    void shouldThrowCurrencyNotFoundExceptionWhenCurrencyNotFound() {
        CurrencyRequest request = new CurrencyRequest();
        request.setCurrency("EURO");
        request.setName("Jan Kowalski");

        ResponseEntity<CurrencyValueResponse[]> mockResponseEntity = ResponseEntity.ok(null);

        when(restTemplate.exchange(eq("http://api.nbp.pl/api/exchangerates/tables/A?format=json"), eq(HttpMethod.GET),
                eq(null), eq(CurrencyValueResponse[].class))).thenReturn(mockResponseEntity);

        assertThrows(CurrencyNotFoundException.class, () -> currencyService.getCurrentCurrencyValue(request));
        verify(currencyRepository, never()).save(any());
    }

    @Test
    void shouldThrowExternalAPIExceptionOnExternalAPIError() {
        CurrencyRequest request = new CurrencyRequest();
        request.setCurrency("EUR");
        request.setName("Jan Kowalski");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(CurrencyValueResponse[].class)))
                .thenThrow(new ExternalAPIException("External API error"));

        assertThrows(ExternalAPIException.class, () -> currencyService.getCurrentCurrencyValue(request));
        verify(currencyRepository, never()).save(any());
    }

    @Test
    void shouldGetAllCurrencyRequests() {
        List<CurrencyRequestData> expectedRequests = new ArrayList<>();
        expectedRequests.add(new CurrencyRequestData());
        expectedRequests.add(new CurrencyRequestData());

        when(currencyRepository.findAll()).thenReturn(expectedRequests);

        List<CurrencyRequestData> requests = currencyService.getAllCurrencyRequests();

        assertEquals(expectedRequests.size(), requests.size());
        verify(currencyRepository, times(1)).findAll();
    }
}
