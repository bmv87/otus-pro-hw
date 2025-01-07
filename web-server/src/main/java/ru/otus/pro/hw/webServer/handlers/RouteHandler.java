package ru.otus.pro.hw.webServer.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pro.hw.webServer.exceptions.NotAcceptableException;
import ru.otus.pro.hw.webServer.exceptions.ResponseException;
import ru.otus.pro.hw.webServer.handlers.invokers.*;
import ru.otus.pro.hw.webServer.http.Constants;
import ru.otus.pro.hw.webServer.http.HttpContext;
import ru.otus.pro.hw.webServer.models.ByteArrayBody;
import ru.otus.pro.hw.webServer.routing.File;
import ru.otus.pro.hw.webServer.routing.Route;
import ru.otus.pro.hw.webServer.routing.RouteParamsCollector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class RouteHandler implements HttpContextHandler {
    private static final Logger logger = LoggerFactory.getLogger(RouteHandler.class);

    private final Route route;
    private final Method method;
    private final RouteParamsCollector paramsCollector;
    private final ProcessorInvoker processorInvoker;

    public RouteHandler(Route route, Method method) {
        this.route = route;
        this.method = method;

        var fileAnnotations = method.getAnnotation(File.class);
        boolean isFileResponse = fileAnnotations != null;

        if (isFileResponse && !ByteArrayBody.class.isAssignableFrom(method.getReturnType())) {
            throw new ResponseException("Некорректный тип класса в параметрах метода.");
        }
        this.paramsCollector = new RouteParamsCollector(route, method.getParameters());

        if (isFileResponse) {
            this.processorInvoker = new FileContentInvoker(method);
        } else if (method.getReturnType().equals(Void.TYPE)) {
            this.processorInvoker = new NoContentInvoker(method);
        } else if (method.getReturnType().equals(byte[].class)) {
            this.processorInvoker = new BytesContentInvoker(method);
        } else {
            this.processorInvoker = new JSONContentInvoker(method);
        }
    }

    @Override
    public void execute(HttpContext context) throws IOException {
        logger.debug("RouteHandler execute");
        logger.debug(route.toString());
        var contentType = context.getRequest().getHeaders().get(Constants.Headers.CONTENT_TYPE.toLowerCase());
        var contentDisposition = context.getRequest().getHeaders().get(Constants.Headers.CONTENT_DISPOSITION.toLowerCase());
        if (contentDisposition == null &&
                contentType != null &&
                (!contentType.equalsIgnoreCase(Constants.MimeTypes.JSON) && !contentType.equalsIgnoreCase(Constants.MimeTypes.TEXT))) {
            throw new NotAcceptableException("Тип данных для передачи не поддерживается " + contentType);
        }
        try {
            List<Object> params = paramsCollector.collect(context);

            var response = context.getResponse();
            if (params.isEmpty()) {
                processorInvoker.tryInvoke(response);
            } else {
                processorInvoker.tryInvokeWithParams(response, params);
            }
            response.send();
        } catch (InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new ResponseException(String.format("Ошибка выполнения метода %s класса %s", method.getName(), method.getDeclaringClass().getSimpleName()), e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ResponseException ex) {
                throw ex;
            } else {
                throw new ResponseException(String.format("Ошибка выполнения метода %s класса %s", method.getName(), method.getDeclaringClass().getSimpleName()), e);
            }
        }
    }
}
