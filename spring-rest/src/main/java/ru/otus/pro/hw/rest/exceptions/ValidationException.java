package ru.otus.pro.hw.rest.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Getter
public class ValidationException extends ServiceException {

    private Map<String, List<String>> errors;

    public ValidationException() {
        super();
        responseCode = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public ValidationException(String message) {
        super(message);
        responseCode = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public ValidationException(String message, Map<String, List<String>> errors) {
        this(message);
        this.errors = errors;
    }
}