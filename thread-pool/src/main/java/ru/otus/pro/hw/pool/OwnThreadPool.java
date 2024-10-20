package ru.otus.pro.hw.pool;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class OwnThreadPool implements ExecutorService {
    private final LinkedList<Runnable> taskQueue = new LinkedList<>();
    private volatile boolean isShutdown = false;
    private final ReentrantLock takeLock = new ReentrantLock();
    private final ReentrantLock putLock = new ReentrantLock();

    public OwnThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            new Thread(new TaskWorker()).start();
        }
    }

    @Override
    public void shutdown() throws InterruptedException {
        isShutdown = true;
        try {
            takeLock.lockInterruptibly();
            putLock.lock();
            taskQueue.clear();

        } finally {
            takeLock.unlock();
            putLock.unlock();
        }
    }

    @Override
    public void execute(Runnable command) {
        if (isShutdown) {
            throw new IllegalStateException("Thread pool is shutdown");
        }
        putLock.lock();
        try {
            if (command == null)
                throw new NullPointerException();
            taskQueue.add(command);
        } finally {
            putLock.unlock();
        }
    }

    private final class TaskWorker implements Runnable {

        @Override
        public void run() {
            while (!isShutdown) {
                try {
                    takeLock.lockInterruptibly();
                    Runnable nextTask = taskQueue.poll();
                    if (nextTask != null) {
                        nextTask.run();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    takeLock.unlock();
                }
            }
        }
    }
}
