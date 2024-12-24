package ru.otus.pro.hw.webServer.exceptions;

import ru.otus.pro.hw.webServer.http.StatusCode;
import ru.otus.pro.hw.webServer.models.ErrorVM;

public class ResponseException extends RuntimeException {
    protected StatusCode code;
    protected String description;

    public ResponseException(String message) {
        super(message);
        code = StatusCode.INTERNAL_SERVER_ERROR;
    }

    public ResponseException(String message, Throwable cause) {
        super(message, cause);
        code = StatusCode.INTERNAL_SERVER_ERROR;
        description = cause.getMessage();
    }

    public ResponseException(Throwable cause) {
        super(cause);
        code = StatusCode.INTERNAL_SERVER_ERROR;
        description = cause.getMessage();
    }

    public ErrorVM getModelForResponse() {
        return new ErrorVM(code.getCode(), code, this.getMessage(), this.description);
    }
}
