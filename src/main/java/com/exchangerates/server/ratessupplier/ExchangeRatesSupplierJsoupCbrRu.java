package com.exchangerates.server.ratessupplier;

import com.exchangerates.util.Validator;
import com.exchangerates.util.pojo.Currency;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

public class ExchangeRatesSupplierJsoupCbrRu implements ExchangeRatesSupplier {

    private static final String CBR_RU_URL = "https://www.cbr.ru/eng/currency_base/daily/";
    private static final String USER_AGENT = "Chrome/123.0.6312.106";
    private static final String REFERRER = "https://google.com";
    private static final String QUERY = "table > tbody > tr";

    private static final Document DOCUMENT;

    static {
        try {
            DOCUMENT = createJsoupDocument();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Document createJsoupDocument() throws IOException {
        return Jsoup
                .connect(CBR_RU_URL)
                .userAgent(USER_AGENT)
                .referrer(REFERRER)
                .get();
    }

    @Override
    public Set<Currency> getAll() {
        Elements currenciesTable = selectCurrenciesTable();
        return parseCurrenciesTable(currenciesTable);
    }

    private static Elements selectCurrenciesTable() {
        Elements currenciesTable;
        synchronized (DOCUMENT) {
            currenciesTable = DOCUMENT.select(QUERY);
        }
        return currenciesTable;
    }

    private static Set<Currency> parseCurrenciesTable(Elements currenciesTable) {
        Validator.requireNonNull(currenciesTable);

        Set<Currency> exchangeRates = new LinkedHashSet<>();

        int currenciesCount = currenciesTable.size();
        for (int i = 1; i < currenciesCount; i++) {
            Elements currencyInfo = currenciesTable.get(i).children();
            Currency currency = parseCurrency(currencyInfo);
            exchangeRates.add(currency);
        }

        return exchangeRates;
    }

    private static Currency parseCurrency(Elements currencyAsElements) {
        Validator.requireNonNull(currencyAsElements);

        String digitalCode = currencyAsElements.get(0).text();
        String letterCode = currencyAsElements.get(1).text();
        int unit = Integer.parseInt(currencyAsElements.get(2).text());
        String currencyName = currencyAsElements.get(3).text();
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(currencyAsElements.get(4).text()));

        return new Currency(digitalCode, letterCode, unit, currencyName, rate);
    }
}
