package ru.otus.pro.hw.crud.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends ServiceException {

    public ValidationException() {
        super();
        responseCode = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public ValidationException(String message) {
        super(message);
        responseCode = HttpStatus.UNPROCESSABLE_ENTITY;
    }
}