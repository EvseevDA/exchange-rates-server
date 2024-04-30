package com.exchangerates.server.logic;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

/**
 * This class is a skeleton class for classes that implement the logic for processing client requests.
 * The processing logic is defined in the inheriting class by overriding the handleRequest method.<br>
 * @see ExchangeRatesRequestController
 * @since 19.0.1
 * @author Evseev Dmitry
 */
public abstract class ClientRequestController implements Runnable {

    private final Socket clientSocket;

    /**
     * Initializes client socket which request will be handled.
     * @param clientSocket socket object by which will be initialized client socket which request will be handled.
     * @throws NullPointerException if clientSocket is null
     */
    public ClientRequestController(Socket clientSocket) {
        this.clientSocket = Objects.requireNonNull(clientSocket);
    }

    protected Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Runs the logic for processing the client request. When the request is processed, closes the client socket.
     */
    @Override
    public final void run() {
        try (clientSocket) {
            handleRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * In classes that inherit from this class and define the logic for processing a client request,
     * this method must be overridden to indicate how the client request should be processed.
     */
    protected abstract void handleRequest();
}
