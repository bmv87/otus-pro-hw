package ru.otus.pro.hw.webServer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pro.hw.webServer.http.Constants;
import ru.otus.pro.hw.webServer.http.HttpContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OptionsRequestHandler implements HttpContextHandler {
    private Map<String, String> headers = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(OptionsRequestHandler.class);

    public OptionsRequestHandler(Map<String, String> headers) {
        this.headers.put(Constants.Headers.CONTENT_LENGTH, "0");
        this.headers.put(Constants.Headers.CONNECTION, "keep-alive");

        for (var header : headers.entrySet()) {
            if (!this.headers.containsKey(header.getKey())) {
                this.headers.put(header.getKey(), header.getValue());
            }
        }
    }

    @Override
    public void execute(HttpContext context) throws IOException {
        logger.debug("OptionsRequestHandler execute");

        var response = context.getResponse();
        for (var header : this.headers.entrySet()) {
            response.addHeader(header.getKey(), header.getValue());
        }
        response.ok().send();
    }
}
