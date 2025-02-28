package com.dexwin.currencyconverter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * TODO: Implementation of this class has to be backed by https://api.exchangerate.host/latest?base=EUR&symbols=AUD,CAD,CHF,CNY,GBP,JPY,USD
 */
@Service
public class CurrencyExchangeRateService  {


    private static final String API_URL = "https://api.exchangerate.host/convert";
    private static final String ACCESS_KEY = "323285b11096537542789b39511772d4";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Constructor injection
    public CurrencyExchangeRateService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Double convertCurrency(String from, String to, double amount) {
        try {
            // Build the API URL
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(API_URL)
                    .queryParam("access_key", ACCESS_KEY)
                    .queryParam("from", from)
                    .queryParam("to", to)
                    .queryParam("amount", amount);

            String apiUrl = builder.toUriString();
            System.out.println("API URL: " + apiUrl); // Log the API URL

            // Make the HTTP request
            String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

            // Log the raw JSON response
            System.out.println("Raw JSON Response: " + jsonResponse);

            // Parse the JSON response
            JsonNode response = objectMapper.readTree(jsonResponse);

            // Log the parsed response
            System.out.println("Parsed Response: " + response);

            // Check for errors in the response
            if (response == null || !response.has("success")) {
                System.err.println("Invalid API response: " + response);
                return null;
            }

            if (!response.get("success").asBoolean()) {
                System.err.println("API Error: " + response.get("error"));
                return null;
            }

            // Extract the result
            return response.get("result").asDouble();
        } catch (Exception e) {
            System.err.println("Error fetching exchange rates: " + e.getMessage());
            return null;
        }
    }
}
