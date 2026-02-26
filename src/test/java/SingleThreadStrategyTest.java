import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.dispatcher.strategy.SingleThreadStrategy;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.util.SenderRegistry;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.mockito.Mockito.*;

class SingleThreadStrategyTest {

    @Test
    void shouldCallProcessTaskForEachNotification() {
        Queue<Notification> queue = new ConcurrentLinkedQueue<>();
        queue.add(mock(Notification.class));
        queue.add(mock(Notification.class));

        Dispatcher.Metrics metrics = mock(Dispatcher.Metrics.class);
        SenderRegistry registry = mock(SenderRegistry.class);

        try (MockedStatic<Dispatcher> mocked = mockStatic(Dispatcher.class)) {

            SingleThreadStrategy strategy = new SingleThreadStrategy();
            strategy.dispatch(queue, metrics, registry);

            mocked.verify(
                    () -> Dispatcher.processTask(any(), eq(metrics), eq(registry)),
                    times(2)
            );
        }
    }
}