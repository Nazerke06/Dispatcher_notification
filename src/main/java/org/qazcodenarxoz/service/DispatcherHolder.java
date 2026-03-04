package org.qazcodenarxoz.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class DispatcherHolder {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherHolder.class);
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void dispatch(String message) {
        executor.submit(() -> {
            log.info("Processing message: {}", message);
            try {
                Thread.sleep(1000); // имитация работы
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Task interrupted: {}", message, e);
            }
            logger.info("Finished: {}", message);
        });
    }

    public void shutdown() {
        executor.shutdown();
        logger.info("Executor shutdown initiated");
    }
}