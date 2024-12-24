package ru.otus.pro.hw.webServer.handlers;

import ru.otus.pro.hw.webServer.http.HttpContext;

import java.io.IOException;

public interface HttpContextHandler {
    void execute(HttpContext context) throws IOException;
}
