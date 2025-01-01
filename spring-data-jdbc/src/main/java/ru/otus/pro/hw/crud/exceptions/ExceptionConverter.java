package ru.otus.pro.hw.crud.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import ru.otus.pro.hw.crud.models.ErrorVM;

public interface ExceptionConverter {

    public ErrorVM getRequestBodyModel(Throwable ex, Boolean needStack);

    public String shortUUID();

    public boolean isCause(Class<? extends Throwable> expected, Throwable exc);

    public Throwable getError(HttpServletRequest request);
}
