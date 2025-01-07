package ru.otus.pro.hw.webServer.handlers;

import ru.otus.pro.hw.webServer.exceptions.NotFoundException;
import ru.otus.pro.hw.webServer.http.HttpContext;

import java.io.IOException;

public class NotFoundHandler implements HttpContextHandler {
    private final String route;

    public NotFoundHandler(String route) {
        this.route = route;
    }

    @Override
    public void execute(HttpContext context) throws IOException {
        throw new NotFoundException("Маршрут не зарегистрирован. " + route);
    }
}
