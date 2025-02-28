package com.dexwin.currencyconverter.controller;

import com.dexwin.currencyconverter.service.CurrencyExchangeRateService;
import com.dexwin.currencyconverter.service.CurrencyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

//    private final CurrencyExchangeRateService currencyExchangeRateService;
//
//    // Constructor injection
//    public CurrencyController(CurrencyExchangeRateService currencyExchangeRateService) {
//        this.currencyExchangeRateService = currencyExchangeRateService;
//    }

    @GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public double convert(
            @RequestParam("source") String source,
            @RequestParam("target") String target,
            @RequestParam("amount") double amount) {

        return currencyService.convert(source, target, amount);
    }

}
