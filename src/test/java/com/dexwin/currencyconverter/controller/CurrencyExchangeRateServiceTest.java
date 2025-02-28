package com.dexwin.currencyconverter.controller;

import com.dexwin.currencyconverter.service.CurrencyExchangeRateService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyExchangeRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CurrencyExchangeRateService exchangeRateService;

    @InjectMocks // Injects the mocks into the service
    private CurrencyExchangeRateService currencyExchangeRateService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }


    @Test
    public void testConvertCurrency_Success()  throws Exception {

        // Mock the service to return the expected result (85.0)
        when(exchangeRateService.convertCurrency(eq("EUR"), eq("GBP"), eq(100.0))).thenReturn(85.0);

        // Call the method under test
        Double result = exchangeRateService.convertCurrency("EUR", "GBP", 100.0);

        // Assert that the result is not null and contains the expected value
        assertNotNull(result);
        assertEquals(85.0, result);

        //verify(currencyExchangeRateService).convertCurrency(eq("EUR"), eq("GBP"), eq(100.0));
    }

    @Test
    public void testConvertCurrency_ApiError() throws Exception {
        // Mock the API response with an error
        String jsonResponse = "{\"success\":false,\"error\":{\"code\":401,\"type\":\"invalid_from_currency\",\"info\":\"Invalid 'from' currency\"}}";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        // Mock the ObjectMapper
        JsonNode mockJsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonResponse)).thenReturn(mockJsonNode);
        when(mockJsonNode.get("success")).thenReturn(mock(JsonNode.class));
        when(mockJsonNode.get("success").asBoolean()).thenReturn(false);

        // Test the conversion
        Double result = currencyExchangeRateService.convertCurrency("XYZ", "GBP", 100);

        // Verify the result
        assertNull(result);
    }

    @Test
    public void testConvertCurrency_InvalidResponse() throws Exception {
        // Mock an invalid API response (missing "result" field)
        String jsonResponse = "{\"success\":true}";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        // Mock the ObjectMapper
        JsonNode mockJsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonResponse)).thenReturn(mockJsonNode);
        when(mockJsonNode.get("success")).thenReturn(mock(JsonNode.class));
        when(mockJsonNode.get("success").asBoolean()).thenReturn(true);
        when(mockJsonNode.has("result")).thenReturn(false);

        // Test the conversion
        Double result = currencyExchangeRateService.convertCurrency("EUR", "GBP", 100);

        // Verify the result
        assertNull(result);
    }

    @Test
    public void testConvertCurrency_Exception() throws Exception {
        // Simulate an exception during the API call
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException("API call failed"));

        // Test the conversion
        Double result = currencyExchangeRateService.convertCurrency("EUR", "GBP", 100);

        // Verify the result
        assertNull(result);
    }
}