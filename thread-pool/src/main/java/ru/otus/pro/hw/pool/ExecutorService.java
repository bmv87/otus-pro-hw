package ru.otus.pro.hw.pool;

import java.util.concurrent.Executor;

public interface ExecutorService extends Executor {

    void shutdown() throws InterruptedException;
}
