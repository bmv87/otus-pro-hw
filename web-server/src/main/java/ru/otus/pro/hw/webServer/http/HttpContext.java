package ru.otus.pro.hw.webServer.http;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class HttpContext implements AutoCloseable {
    private Socket connection;
    @Getter
    private HttpRequest request;
    @Getter
    private HttpResponse response;
    private static final Logger logger = LoggerFactory.getLogger(HttpContext.class);

    public HttpContext(Socket connection) {
        this.connection = connection;
        try {
            request = new HttpRequest(connection.getInputStream());
            response = new HttpResponse(connection.getOutputStream(), request.getProtocol());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания объекта HttpContext.", e);
        }
    }

    @Override
    public void close() {
        try {
            request = null;
            response = null;
            if (connection != null && !connection.isClosed() && connection.isConnected()) {
                connection.close();
            }
            connection = null;
        } catch (IOException e) {
            logger.error("Ошибка завершения работы HttpContext", e);
        }
    }
}
