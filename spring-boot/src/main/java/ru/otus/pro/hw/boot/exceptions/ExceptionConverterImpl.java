package ru.otus.pro.hw.boot.exceptions;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import ru.otus.pro.hw.boot.models.ErrorVM;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ExceptionConverterImpl implements ExceptionConverter {
    @Override
    public ErrorVM getRequestBodyModel(Throwable ex, Boolean needStack) {
        String stackTrace = "";
        if (needStack) {
            stackTrace = Arrays.stream(ex.getStackTrace())
                    .map(s -> s.toString())
                    .collect(Collectors.joining("\r\n\t"));
        }
        var errorVm = new ErrorVM();
        if (ex instanceof ServiceException exep) {

            errorVm.setCode(exep.getResponseCode().value());
            errorVm.setMessage(exep.getMessage());
            errorVm.setStackTrace(stackTrace);
        } else {
            errorVm.setCode(500);
            errorVm.setMessage(ex.getMessage());
            errorVm.setStackTrace(stackTrace);
        }

        return errorVm;
    }

    @Override
    public String shortUUID() {
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes())
                .getLong();
        return Long.toString(l, Character.MAX_RADIX);
    }

    @Override
    public boolean isCause(Class<? extends Throwable> expected, Throwable exc) {
        return expected.isInstance(exc)
                || (exc != null && isCause(expected, exc.getCause()));
    }

    @Override
    public Throwable getError(HttpServletRequest request) {
        if (request.getAttribute(
                RequestDispatcher.ERROR_EXCEPTION) != null) {
            return (Throwable) request
                    .getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        }
        return null;
    }
}
