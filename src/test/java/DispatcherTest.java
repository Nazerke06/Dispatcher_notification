import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.notification.OTPNotification;
import org.qazcodenarxoz.repository.NotificationRepository;
import org.qazcodenarxoz.sender.Sender;
import org.qazcodenarxoz.util.SenderRegistry;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DispatcherTest {

    private NotificationRepository<Notification> repository;
    private Dispatcher<Notification> dispatcher;

    @BeforeEach
    void setUp() {
        repository = new NotificationRepository<>();

        SenderRegistry registry = new SenderRegistry("dummy") {
            @Override
            public Optional<Sender<?>> getSender(String channel) {
                return Optional.of(notification -> {
                });
            }
        };

        dispatcher = new Dispatcher<>(repository, registry);
    }

    @Test
    void sendAll_shouldProcessNotificationsSuccessfully() throws InterruptedException {
        Notification notification = new Notification(1L, "EMAIL", "user", "text");
        repository.add(notification);

        dispatcher.sendAll(1);

        assertTrue(repository.getAll().isEmpty(), "Repository should be empty after sending");
    }

    @Test
    void sendAll_shouldHandleNoSenderWithoutException() {
        SenderRegistry emptyRegistry = new SenderRegistry("dummy") {
            @Override
            public Optional<Sender<?>> getSender(String channel) {
                return Optional.empty();
            }
        };

        Dispatcher<Notification> dispatcherNoSender = new Dispatcher<>(repository, emptyRegistry);

        repository.add(new Notification(1L, "UNKNOWN", "user", "text"));

        assertDoesNotThrow(() -> dispatcherNoSender.sendAll(1),
                "Dispatcher should not throw an exception when sender is missing");
    }

    @Test
    void processTask_shouldHandleOTPNotification() throws InterruptedException {
        OTPNotification otp = new OTPNotification(2L, "EMAIL", "user", "otp","String");
        repository.add(otp);

        dispatcher.sendAll(1);

        assertTrue(repository.getAll().isEmpty(), "Repository should be empty after sending OTP");
    }

    @Test
    void processTask_shouldHandleSenderException() {
        SenderRegistry faultyRegistry = new SenderRegistry("dummy") {
            @Override
            public Optional<Sender<?>> getSender(String channel) {
                return Optional.of(n -> { throw new RuntimeException("fail"); });
            }
        };
        Dispatcher<Notification> dispatcherFaulty = new Dispatcher<>(repository, faultyRegistry);

        Notification n = new Notification(3L, "EMAIL", "user", "text");
        repository.add(n);

        assertDoesNotThrow(() -> dispatcherFaulty.sendAll(1),
                "Dispatcher should handle sender exceptions gracefully");
    }

    @Test
    void printStats_shouldWork() throws InterruptedException {
        Notification n = new Notification(4L, "EMAIL", "user", "text");
        repository.add(n);

        dispatcher.sendAll(1);

        assertDoesNotThrow(dispatcher::printStats, "Printing stats should not throw");
    }
}
