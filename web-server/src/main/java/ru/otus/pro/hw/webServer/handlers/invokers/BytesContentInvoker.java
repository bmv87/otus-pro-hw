package ru.otus.pro.hw.webServer.handlers.invokers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pro.hw.webServer.http.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BytesContentInvoker extends ProcessorInvoker{
    private static final Logger logger = LoggerFactory.getLogger(BytesContentInvoker.class);

    private final Method method;

    public BytesContentInvoker(Method method) {
        this.method = method;
    }

    @Override
    public void tryInvoke(HttpResponse response) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        var inst = method.getDeclaringClass().getConstructor().newInstance();
        try {
            response.okHtml((byte[]) method.invoke(inst));
        } finally {
            close(inst);
        }
    }

    @Override
    public void tryInvokeWithParams(HttpResponse response, List<Object> params) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        var inst = method.getDeclaringClass().getConstructor().newInstance();
        try {
            response.okHtml((byte[])method.invoke(inst, params.toArray()));
        } finally {
            close(inst);
        }
    }

    private <T> void close(T inst) {
        try {
            tryClose(inst);
        } catch (Exception e) {
            logger.error("Ошибка при закрытии ресурса.", e);
        }
    }

}
