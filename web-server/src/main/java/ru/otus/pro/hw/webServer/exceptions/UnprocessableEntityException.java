package ru.otus.pro.hw.webServer.exceptions;

import ru.otus.pro.hw.webServer.http.StatusCode;

public class UnprocessableEntityException extends ResponseException {
    public UnprocessableEntityException(String message) {
        super(message);
        code = StatusCode.UNPROCESSABLE_ENTITY;
    }

    public UnprocessableEntityException(String message, Throwable cause) {
        super(message, cause);
        code = StatusCode.UNPROCESSABLE_ENTITY;
    }

    public UnprocessableEntityException(Throwable cause) {
        super(cause);
        code = StatusCode.UNPROCESSABLE_ENTITY;
    }
}
