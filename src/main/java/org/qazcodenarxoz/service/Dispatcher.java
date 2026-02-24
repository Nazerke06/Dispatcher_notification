package org.qazcodenarxoz.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void dispatch(String message) {
        executor.submit(() -> {
            System.out.println("Processing message: " + message);
            try {
                Thread.sleep(1000); // имитация работы
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Finished: " + message);
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}