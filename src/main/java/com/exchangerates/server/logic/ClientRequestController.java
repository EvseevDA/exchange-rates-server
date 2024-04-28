package com.exchangerates.server.logic;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public abstract class ClientRequestController implements Runnable {

    private final Socket clientSocket;

    public ClientRequestController(Socket clientSocket) {
        this.clientSocket = Objects.requireNonNull(clientSocket);
    }

    protected Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public final void run() {
        try (clientSocket) {
            handleRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void handleRequest();
}
