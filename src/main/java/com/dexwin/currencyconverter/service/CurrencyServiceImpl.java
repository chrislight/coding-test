package com.dexwin.currencyconverter.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyExchangeRateService exchangeRateService;

    public CurrencyServiceImpl(CurrencyExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public double convert(String from, String to, double amount) {
        // Perform the conversion
        Double result = exchangeRateService.convertCurrency(from, to, amount);

        // Return the result or indicate failure
        return result != null ? result : -1;
    }
}
