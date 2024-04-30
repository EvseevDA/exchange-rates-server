package com.exchangerates.server;

import com.exchangerates.server.logic.ClientRequestController;
import com.exchangerates.server.logic.ExchangeRatesRequestController;
import com.exchangerates.server.ratessupplier.ExchangeRatesSupplier;
import com.exchangerates.server.ratessupplier.ExchangeRatesSupplierJsoupCbrRu;
import com.exchangerates.util.constants.Constants;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * This defines a method that executes the server's work.
 * @see ClientRequestController
 * @see ExchangeRatesRequestController
 * @see ExchangeRatesSupplier
 * @see ExchangeRatesSupplierJsoupCbrRu
 * @since 19.0.1
 * @author Evseev Dmitry
 */
public class Main {

    /**
     * Here for everyone client request is created on a server socket,
     * a new thread in which the request will be processed, a new controller and a new currency rate provider.
     * @param args not used
     */
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