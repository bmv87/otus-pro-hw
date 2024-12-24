package ru.otus.pro.hw.webServer.exceptions;

import ru.otus.pro.hw.webServer.http.StatusCode;

public class ForbiddenException extends ResponseException {
    public ForbiddenException(String message) {
        super(message);
        code = StatusCode.FORBIDDEN;
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
        code = StatusCode.FORBIDDEN;
    }

    public ForbiddenException(Throwable cause) {
        super(cause);
        code = StatusCode.FORBIDDEN;
    }
}
