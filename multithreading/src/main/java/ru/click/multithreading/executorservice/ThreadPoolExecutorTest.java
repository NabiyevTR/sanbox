package ru.click.multithreading.executorservice;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadPoolExecutorTest {

    private static final int POOL_THREADS_NUMBER = 5;
    private static final int THREADS_NUMBER = 100;


    public static void main(String[] args) {
        test_2();

    }

    private static void test_1() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                POOL_THREADS_NUMBER,
                POOL_THREADS_NUMBER,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < THREADS_NUMBER; i++) {
            executor.submit(new Task(i));
        }
        log.info("All task are submitted");

        executor.shutdown();
    }

    private static void test_2() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                0,
                POOL_THREADS_NUMBER,
                0L,
                TimeUnit.MILLISECONDS,
               new LinkedBlockingQueue<>(POOL_THREADS_NUMBER));

        for (int i = 0; i < THREADS_NUMBER; i++) {
            executor.submit(new Task(i));
        }
        log.info("All task are submitted");

        executor.shutdown();
    }

}
