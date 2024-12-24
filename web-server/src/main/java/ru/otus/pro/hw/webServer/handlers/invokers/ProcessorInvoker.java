package ru.otus.pro.hw.webServer.handlers.invokers;

import ru.otus.pro.hw.webServer.http.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class ProcessorInvoker {

    public abstract void tryInvoke(HttpResponse response) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException;

    public abstract void tryInvokeWithParams(HttpResponse response, List<Object> params) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException;


    <T> void tryClose(T instance) throws Exception {
        if (instance instanceof AutoCloseable closable) {
            closable.close();
        }
    }
}
