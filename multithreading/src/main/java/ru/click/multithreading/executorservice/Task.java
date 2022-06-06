package ru.click.multithreading.executorservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
public class Task implements Runnable {

    private static final int MIN_SLEEP_TIME = 100;
    private static final int MAX_SLEEP_TIME = 2000;


    private final int number;

    @Override
    public void run() {

        int sleepTime = getSleepTime();

        log.info("Thread with number {} has started. Sleep time {}", number, sleepTime);

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Thread with number {} has finished. Sleep time {}", number, sleepTime);
    }

    private int getSleepTime() {
        Random random = new Random();
        return random.nextInt(MAX_SLEEP_TIME - MIN_SLEEP_TIME) + MIN_SLEEP_TIME;
    }
}
