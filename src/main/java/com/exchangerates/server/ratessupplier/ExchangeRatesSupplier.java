package com.exchangerates.server.ratessupplier;

import com.exchangerates.util.pojo.Currency;

import java.util.Set;

/**
 * Any exchange rate supplier must implement this interface.<br>
 * Set is chosen as the collection to be returned to ensure that objects cannot be duplicated within it.
 * @see ExchangeRatesSupplierJsoupCbrRu
 * @since 19.0.1
 * @author Evseev Dmitry
 */
public interface ExchangeRatesSupplier {
    Set<Currency> getAll();
}
