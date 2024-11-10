package ru.otus.pro.hw.pool;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OwnThreadPool implements ExecutorService {
    private final LinkedList<Runnable> taskQueue = new LinkedList<>();
    private volatile boolean isShutdown = false;
    private final Lock writeLock = new ReentrantLock();
    private final Condition isNotEmptyPool = writeLock.newCondition();

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
            isNotEmptyPool.signalAll();
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
                System.out.println("Write to queue " + Thread.currentThread().getName());
            taskQueue.add(command);
        } finally {
            isNotEmptyPool.signal();
            writeLock.unlock();
        }
    }

    private final class TaskWorker implements Runnable {

        @Override
        public void run() {
            while (!isShutdown) {
                try {
                    System.out.println("Run  TaskWorker " + Thread.currentThread().getName());
                    writeLock.lockInterruptibly();

                    if (taskQueue.isEmpty() && !isShutdown) {
                        isNotEmptyPool.await();
                    }
                    System.out.println("Reed from queue " + Thread.currentThread().getName());
                    try {
                        Runnable nextTask = taskQueue.poll();
                        if (nextTask != null) {
                            nextTask.run();
                        }
                    } finally {
                        writeLock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
