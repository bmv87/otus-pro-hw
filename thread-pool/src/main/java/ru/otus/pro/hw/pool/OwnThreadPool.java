package ru.otus.pro.hw.pool;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class OwnThreadPool implements ExecutorService {
    private final LinkedList<Runnable> taskQueue = new LinkedList<>();
    private volatile boolean isShutdown = false;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public OwnThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            new Thread(new TaskWorker()).start();
        }
    }

    @Override
    public void shutdown() {
        isShutdown = true;
        writeLock.lock();
        try {
            taskQueue.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void execute(Runnable command) {
        if (isShutdown) {
            throw new IllegalStateException("Thread pool is shutdown");
        }
        writeLock.lock();
        try {
            if (command == null)
                throw new NullPointerException();
            taskQueue.add(command);
        } finally {
            writeLock.unlock();
        }
    }

    private final class TaskWorker implements Runnable {

        @Override
        public void run() {
            while (!isShutdown) {
                try {
                    readLock.lockInterruptibly();

                    try {
                        Runnable nextTask = taskQueue.poll();
                        if (nextTask != null) {
                            nextTask.run();
                        }
                    } finally {
                        readLock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
