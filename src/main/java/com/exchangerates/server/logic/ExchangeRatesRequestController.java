package com.exchangerates.server.logic;

import com.exchangerates.server.ratessupplier.ExchangeRatesSupplier;
import com.exchangerates.server.ratessupplier.ExchangeRatesSupplierJsoupCbrRu;
import com.exchangerates.util.Validator;
import com.exchangerates.util.constants.Constants;
import com.exchangerates.util.pojo.Currency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * This controller processes user requests for exchange rates.
 * @see ExchangeRatesSupplier
 * @see ExchangeRatesSupplierJsoupCbrRu
 * @see Currency
 * @see Constants
 * @since 19.0.1
 * @author Evseev Dmitry
 */
public class ExchangeRatesRequestController extends ClientRequestController {

    private final ExchangeRatesSupplier exchangeRatesSupplier;

    /**
     * @param clientSocket socket from which request got
     * @param exchangeRatesSupplier interface which supplies exchange rates
     * @throws NullPointerException if clientSocket or exchangeRatesSupplier is null
     */
    public ExchangeRatesRequestController(Socket clientSocket, ExchangeRatesSupplier exchangeRatesSupplier) {
        super(clientSocket);
        this.exchangeRatesSupplier = Objects.requireNonNull(exchangeRatesSupplier);
    }

    /**
     * Processes the client request and sends a response.<br>
     * If the request matches GET_NEW_RATES_REQUEST from com.exchangerates.util.constants.Constants
     * sends a set of Currency objects, this Set contains all the exchange rates that the class
     * implementing ExchangeRatesSupplier can supply.
     * For any other request, com.exchangerates.util.constants.Constants.DEFAULT_RESPONSE will be sent as a response.
     */
    @Override
    protected void handleRequest() {
        try (BufferedReader requestReader = createRequestReader();
             ObjectOutputStream responseWriter = createResponseWriter()) {
            String request = readRequest(requestReader);
            Object response = createResponse(request);
            sendResponse(response, responseWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedReader createRequestReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));
    }

    private ObjectOutputStream createResponseWriter() throws IOException {
        return new ObjectOutputStream(getClientSocket().getOutputStream());
    }

    private static String readRequest(BufferedReader requestReader) throws IOException {
        return Objects.requireNonNull(requestReader).readLine();
    }

    private Object createResponse(String request) {
        Validator.requireNonNull(request);

        if (request.equals(Constants.GET_NEW_RATES_REQUEST)) {
            return exchangeRatesSupplier.getAll();
        }
        return Constants.DEFAULT_RESPONSE;
    }

    private static void sendResponse(Object response, ObjectOutputStream responseWriter) throws IOException {
        Validator.requireNonNull(response, responseWriter);

        responseWriter.writeObject(response);
        responseWriter.flush();
    }
}
