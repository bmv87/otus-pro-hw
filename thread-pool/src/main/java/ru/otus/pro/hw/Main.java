package ru.otus.pro.hw;

import ru.otus.pro.hw.pool.ExecutorService;
import ru.otus.pro.hw.pool.OwnThreadPool;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
   static AtomicInteger taskCount = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = new OwnThreadPool(5);

        for (int i = 0; i < 100; i++) {
            threadPool.execute(() -> {
                taskCount.incrementAndGet();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread " + Thread.currentThread().getName());
            });
        }

        Thread.sleep(2000);
        threadPool.shutdown();
        System.out.println("total tasks executed " + taskCount.get());
    }
}
