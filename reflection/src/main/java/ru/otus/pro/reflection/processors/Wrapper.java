package ru.otus.pro.reflection.processors;

public interface Wrapper {
    InvokeResults tryInvoke(Object classInstance);
    boolean isTest();
}
