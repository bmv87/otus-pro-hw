package ru.otus.pro.hw.webServer.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pro.hw.webServer.http.HttpContext;
import ru.otus.pro.hw.webServer.routing.RouteDispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final ServerSocket serverSocket;
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private final RouteDispatcher dispatcher;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public RequestHandler(Socket connection, ServerSocket serverSocket, RouteDispatcher dispatcher) throws IOException {
        this.socket = connection;
        this.serverSocket = serverSocket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try (var context = new HttpContext(socket)) {
            dispatcher.execute(context, serverSocket);
        } catch (Exception e) {
            logger.error("Ошибка обработки контекста запроса.", e);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                logger.error("Ошибка чистки объекта класса RequestHandler", e);
            }
        }
    }
}
