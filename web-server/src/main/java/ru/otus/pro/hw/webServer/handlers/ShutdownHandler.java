package ru.otus.pro.hw.webServer.handlers;

import ru.otus.pro.hw.webServer.exceptions.NotFoundException;
import ru.otus.pro.hw.webServer.http.HttpContext;

import java.io.IOException;
import java.net.ServerSocket;

public class ShutdownHandler {
    public void execute(ServerSocket serverSocket) throws IOException {
        serverSocket.close();
    }
}
