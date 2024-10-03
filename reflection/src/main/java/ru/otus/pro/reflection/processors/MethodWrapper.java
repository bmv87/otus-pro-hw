package ru.otus.pro.reflection.processors;

import lombok.Getter;
import ru.otus.pro.reflection.annotations.BeforeSuite;
import ru.otus.pro.reflection.annotations.Disabled;
import ru.otus.pro.reflection.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodWrapper implements Wrapper, Comparable<MethodWrapper> {

    @Getter
    private final boolean test;
    @Getter
    private final boolean disabled;
    @Getter
    private final int priority;
    private final Method method;

    public MethodWrapper(Class<?> clazz, Method method) {
        this.method = method;
        this.test = method.isAnnotationPresent(Test.class);
        this.priority = getPriorityValue();
        this.disabled = (clazz.isAnnotationPresent(Disabled.class)) || method.isAnnotationPresent(Disabled.class);
    }

    private int getPriorityValue() {
        if (test) {
            return method.getDeclaredAnnotation(Test.class).priority();
        }
        if (method.isAnnotationPresent(BeforeSuite.class)) {
            return Integer.MAX_VALUE;
        }
        return Integer.MIN_VALUE;
    }

    private String getReason() {
        var dAnnotation = method.getDeclaredAnnotation(Disabled.class);
        if (dAnnotation != null && !dAnnotation.reason().isBlank()) {
            return dAnnotation.reason();
        }
        dAnnotation = method.getDeclaringClass().getDeclaredAnnotation(Disabled.class);
        if (dAnnotation != null && !dAnnotation.reason().isBlank()) {
            return dAnnotation.reason();
        }
        return "";
    }

    public InvokeResults tryInvoke(Object classInstance) {
        if (test && disabled) {
            System.out.printf("Тест %s::%s отключен. Причина: %s %n", method.getDeclaringClass().getSimpleName(), method.getName(), getReason());
            return InvokeResults.NOT_INVOKED;
        }
        if (disabled) {
            return InvokeResults.NOT_INVOKED;
        }
        try {
            if (classInstance == null) {
                throw new NullPointerException("В метод invoke не может быть передан null. Параметр classInstance.");
            }
            method.setAccessible(true);
            method.invoke(classInstance);
            return InvokeResults.SUCCESS;
        } catch (InvocationTargetException e) {
            System.out.printf("Тест %s не пройден. Причина: %s %n",method.getName(), e.getCause().getMessage());
        } catch (Exception e) {
            System.out.printf("Тест %s завершился с ошибкой. %s %n", e.getMessage());
        }
        return InvokeResults.FAILED;
    }

    @Override
    public int compareTo(MethodWrapper o) {
        if (o.priority > priority) {
            return 1;
        }
        if (o.priority == priority) {
            return 0;
        }
        return -1;
    }
}
