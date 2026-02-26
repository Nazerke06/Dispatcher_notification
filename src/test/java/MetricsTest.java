//
//
//import org.junit.jupiter.api.Test;
//import org.qazcodenarxoz.dispatcher.Dispatcher;
//import org.qazcodenarxoz.notification.Notification;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class MetricsTest {
//
//    @Test
//    void recordSuccess_and_recordFail_shouldWork() {
//        Dispatcher.Metrics metrics = new Dispatcher.Metrics();
//
//        Notification n1 = new Notification(1L, "EMAIL", "a", "b");
//        Notification n2 = new Notification(2L, "SMS", "a", "b");
//
//        metrics.recordSuccess(n1, 50);
//        metrics.recordFail(n2, "ERROR");
//
//        assertNotNull(metrics);
//    }
//}
