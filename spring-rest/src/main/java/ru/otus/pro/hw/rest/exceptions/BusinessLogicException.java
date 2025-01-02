package ru.otus.pro.hw.rest.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class BusinessLogicException extends ServiceException {

    private Map<String, List<String>> errors;

    public BusinessLogicException() {
        super();
        responseCode = HttpStatus.BAD_GATEWAY;
    }

    public BusinessLogicException(String message) {
        super(message);
        responseCode = HttpStatus.BAD_REQUEST;
    }

}
