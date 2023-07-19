package com.example.currencyapp.controller;

import com.example.currencyapp.model.CurrencyRequest;
import com.example.currencyapp.model.CurrencyRequestData;
import com.example.currencyapp.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CurrencyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    }

    @Test
    public void shouldReturnCurrencyRequestData() throws Exception {
        CurrencyRequest request = new CurrencyRequest();
        request.setCurrency("EUR");
        request.setName("Jan Kowalski");

        CurrencyRequestData requestData = new CurrencyRequestData();
        requestData.setCurrency("EUR");
        requestData.setName("Jan Kowalski");
        requestData.setDate(LocalDateTime.now());
        requestData.setCurrencyValue(1.2345);

        when(currencyService.getCurrentCurrencyValue(any(CurrencyRequest.class))).thenReturn(requestData);

        mockMvc.perform(post("/currencies/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currency\":\"EUR\",\"name\":\"Jan Kowalski\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.name").value("Jan Kowalski"))
                .andExpect(jsonPath("$.currencyValue").value(1.2345));
    }

    @Test
    public void shouldReturnAllCurrencyRequests() throws Exception {
        CurrencyRequestData requestData1 = new CurrencyRequestData();
        requestData1.setCurrency("EUR");
        requestData1.setName("Jan Kowalski");
        requestData1.setDate(LocalDateTime.now());
        requestData1.setCurrencyValue(1.2345);

        CurrencyRequestData requestData2 = new CurrencyRequestData();
        requestData2.setCurrency("USD");
        requestData2.setName("Anna Nowak");
        requestData2.setDate(LocalDateTime.now());
        requestData2.setCurrencyValue(0.9876);

        List<CurrencyRequestData> requestList = Arrays.asList(requestData1, requestData2);

        when(currencyService.getAllCurrencyRequests()).thenReturn(requestList);

        mockMvc.perform(get("/currencies/requests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].currency").value("EUR"))
                .andExpect(jsonPath("$[0].name").value("Jan Kowalski"))
                .andExpect(jsonPath("$[0].currencyValue").value(1.2345))
                .andExpect(jsonPath("$[1].currency").value("USD"))
                .andExpect(jsonPath("$[1].name").value("Anna Nowak"))
                .andExpect(jsonPath("$[1].currencyValue").value(0.9876));
    }
}



