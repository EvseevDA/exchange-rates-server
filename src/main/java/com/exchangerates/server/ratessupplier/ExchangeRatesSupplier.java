package com.exchangerates.server.ratessupplier;

import com.exchangerates.util.pojo.Currency;

import java.util.Set;

public interface ExchangeRatesSupplier {
    Set<Currency> getAll();
}
