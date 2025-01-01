package ru.otus.pro.hw.crud.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    private final ExceptionConverter exceptionConverter;

    private final boolean needStack;

    public GlobalExceptionHandler(
            @Autowired ExceptionConverter exceptionConverter,
            @Value("${response.exceptions.needStack}") boolean needStack) {
        this.exceptionConverter = exceptionConverter;
        this.needStack = needStack;
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowable(
            final Throwable ex) {

        var errorVM = exceptionConverter.getRequestBodyModel(ex, needStack);
        log.error(errorVM.getMessage(), ex);

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(errorVM.getCode());
        builder.contentType(MediaType.APPLICATION_JSON);
        return builder.body(errorVM);
    }
}