package com.exchangerates.server;

import com.exchangerates.server.logic.ExchangeRatesRequestController;
import com.exchangerates.server.ratessupplier.ExchangeRatesSupplierJsoupCbrRu;
import com.exchangerates.util.constants.Constants;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(Constants.PORT)) {
                new Thread(new ExchangeRatesRequestController(serverSocket.accept(),
                        new ExchangeRatesSupplierJsoupCbrRu())).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}