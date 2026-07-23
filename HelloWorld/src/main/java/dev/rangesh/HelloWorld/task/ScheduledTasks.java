package dev.rangesh.HelloWorld.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Scheduled background tasks demonstrating Spring Boot's @Scheduled features.
 */
@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    /**
     * Periodic background health check scheduled every 5 minutes (300,000 ms), with initial delay of 10s.
     */
    @Scheduled(initialDelay = 10000, fixedRate = 300000)
    public void reportSystemStatus() {
        logger.info("[Scheduled Task - Thread: {}] Periodic system health check at {}",
                Thread.currentThread().getName(), LocalDateTime.now());
    }

    /**
     * Daily maintenance cron task example (runs at midnight 00:00:00 every day).
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void performNightlyCleanup() {
        logger.info("[Scheduled Cron Task] Performing nightly system cleanup at {}", LocalDateTime.now());
    }
}
