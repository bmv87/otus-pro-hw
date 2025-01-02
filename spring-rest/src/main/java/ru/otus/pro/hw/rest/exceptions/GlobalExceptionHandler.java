package ru.otus.pro.hw.rest.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.pro.hw.rest.models.ErrorVM;
import ru.otus.pro.hw.rest.models.ValidationErrorVM;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    private final boolean needStack;

    public GlobalExceptionHandler(@Value("${response.exceptions.needStack}") boolean needStack) {
        this.needStack = needStack;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleHWValidationException(
            final ValidationException ex) {

        var errorVm = new ValidationErrorVM();
        fillServiceExceptionVm(errorVm, ex);
        errorVm.setErrors(ex.getErrors());
        log.error(errorVm.getMessage(), ex);

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(errorVm.getCode());
        builder.contentType(MediaType.APPLICATION_JSON);
        return builder.body(errorVm);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(
            final BindException ex) {

        var errorVm = new ValidationErrorVM();
        errorVm.setCode(400);
        errorVm.setMessage(ex.getMessage());
        if (ex instanceof BindException cex) {
            errorVm.setErrors(cex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, e -> List.of(e.getDefaultMessage()))));
        }

        log.error(errorVm.getMessage(), ex);

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(errorVm.getCode());
        builder.contentType(MediaType.APPLICATION_JSON);
        return builder.body(errorVm);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(
            final ServiceException ex) {

        var errorVm = new ErrorVM();
        fillServiceExceptionVm(errorVm, ex);
        log.error(errorVm.getMessage(), ex);

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(errorVm.getCode());
        builder.contentType(MediaType.APPLICATION_JSON);
        return builder.body(errorVm);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowable(
            final Throwable ex) {

        var errorVm = new ErrorVM();
        if (ex.getCause() instanceof ServiceException se) {
            fillServiceExceptionVm(errorVm, se);
        } else {
            fillAnyOtherExceptionVm(errorVm, ex);
        }
        log.error(errorVm.getMessage(), ex);

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(errorVm.getCode());
        builder.contentType(MediaType.APPLICATION_JSON);
        return builder.body(errorVm);
    }


    private void fillAnyOtherExceptionVm(ErrorVM errorVm, Throwable ex) {
        String stackTrace = "";
        if (needStack) {
            stackTrace = Arrays.stream(ex.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\r\n\t"));
        }
        errorVm.setCode(500);
        errorVm.setMessage(ex.getMessage());
        errorVm.setStackTrace(stackTrace);
    }

    private void fillServiceExceptionVm(ErrorVM errorVm, ServiceException ex) {
        String stackTrace = "";
        if (needStack) {
            stackTrace = Arrays.stream(ex.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\r\n\t"));
        }
        errorVm.setCode(ex.getResponseCode().value());
        errorVm.setMessage(ex.getMessage());
        errorVm.setStackTrace(stackTrace);
    }
}