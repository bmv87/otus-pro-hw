package ru.otus.pro.hw.rest.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends ServiceException {

    public NotFoundException() {
        super();
        responseCode = HttpStatus.NOT_FOUND;
    }

    public NotFoundException(String message) {
        super(message);
        responseCode = HttpStatus.NOT_FOUND;
    }
}