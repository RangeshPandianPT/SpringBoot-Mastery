package dev.rangesh.HelloWorld.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener that handles StudentCreatedEvent asynchronously.
 */
@Component
public class StudentEventListener {

    private static final Logger logger = LoggerFactory.getLogger(StudentEventListener.class);

    @Async
    @EventListener
    public void handleStudentCreatedEvent(StudentCreatedEvent event) {
        logger.info("[Event Listener - Thread: {}] Handling StudentCreatedEvent: {}",
                Thread.currentThread().getName(), event);
        
        // Simulating background action (e.g. notification / email delivery / audit logging)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.info("[Event Listener] Successfully processed onboarding notifications for student: {}",
                event.getStudent().getName());
    }
}
