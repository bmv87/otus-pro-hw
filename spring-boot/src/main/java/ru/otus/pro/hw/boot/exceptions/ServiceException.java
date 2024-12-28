package ru.otus.pro.hw.boot.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {
    protected HttpStatus responseCode;

    public ServiceException() {
        super();
        responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ServiceException(String message) {
        super(message);
        responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
