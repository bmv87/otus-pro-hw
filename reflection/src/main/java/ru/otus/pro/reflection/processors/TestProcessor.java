package ru.otus.pro.reflection.processors;

import ru.otus.pro.reflection.annotations.Disabled;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestProcessor {

    private final static int PRIORITY_MIN = 1;
    private final static int PRIORITY_MAX = 10;
    private static final ExecutorService clientPool = Executors.newFixedThreadPool(4);
    private static final ProcessResult pResult = new ProcessResult();

    private Map<Class<?>, List<Wrapper>> items;

    public TestProcessor(Collector collector) {
        items = collector.collect();
    }

    public void process() {
        try {
            for (var item : items.entrySet()) {

                clientPool.submit(() -> {
                    var result = process(item.getKey(), item.getValue());
                    synchronized (pResult) {
                        pResult.setSuccessCount(result.getSuccessCount() + pResult.getSuccessCount());
                        pResult.setFailedCount(result.getFailedCount() + pResult.getFailedCount());
                        pResult.setDisabledCount(result.getDisabledCount() + pResult.getDisabledCount());
                        pResult.setTotalCount(result.getTotalCount() + pResult.getTotalCount());
                    }
                });
            }
            clientPool.shutdown();
            boolean finished = clientPool.awaitTermination(1, TimeUnit.MINUTES);
            System.out.println(pResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ProcessResult process(Class<?> clazz, List<Wrapper> methods) {
        var result = new ProcessResult();
        result.setTotalCount(methods.stream().filter(Wrapper::isTest).count());
        try {
            var instance = clazz.isAnnotationPresent(Disabled.class) ? null : clazz.getConstructor().newInstance();

            for (var method : methods) {
                var invokeResult = method.tryInvoke(instance);

                if (method.isTest()) {
                    switch (invokeResult) {
                        case SUCCESS -> result.setSuccessCount(result.getSuccessCount() + 1);
                        case FAILED -> result.setFailedCount(result.getFailedCount() + 1);
                        case NOT_INVOKED -> result.setDisabledCount(result.getDisabledCount() + 1);
                    }
                }
            }
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new IllegalArgumentException("Некорректно задан класс", e);
        }
        return result;
    }
}
