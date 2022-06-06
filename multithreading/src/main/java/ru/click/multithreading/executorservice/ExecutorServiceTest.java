package ru.click.multithreading.executorservice;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ExecutorServiceTest {

    private static final int POOL_THREADS_NUMBER = 5;
    private static final int THREADS_NUMBER = 100;


    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(POOL_THREADS_NUMBER);

        for (int i = 0; i < THREADS_NUMBER; i++) {

            executor.submit(new Task(i));
        }

        log.info("All task are submitted");

        executor.shutdown();

    }




}
