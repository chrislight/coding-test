package com.dexwin.currencyconverter.controller;

import com.dexwin.currencyconverter.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService; // Mock the CurrencyService

    @BeforeEach
    void setUp() {
        // No need to call MockitoAnnotations.openMocks(this) when using @MockBean
        this.mockMvc = MockMvcBuilders.standaloneSetup(new CurrencyController(currencyService)).build();
    }

    @Test
    public void should_convert_EUR_to_USD_with_rate_greater_than_1() throws Exception {
        // Mock the conversion rate (EUR to USD is typically greater than 1)
        when(currencyService.convert(anyString(), anyString(), anyDouble())).thenReturn(1.2);

        this.mockMvc.perform(get("/currencies/convert?source=EUR&target=USD&amount=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("1.2"));
    }

    @Test
    public void should_convert_USD_to_EUR_with_rate_less_than_1() throws Exception {
        // Mock the conversion rate (USD to EUR is typically less than 1)
        when(currencyService.convert(anyString(), anyString(), anyDouble())).thenReturn(0.85);

        this.mockMvc.perform(get("/currencies/convert?source=USD&target=EUR&amount=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.85"));
    }
}