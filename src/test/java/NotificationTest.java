//
//import org.junit.jupiter.api.Test;
//import org.qazcodenarxoz.notification.Notification;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class NotificationTest {
//
//    @Test
//    void equals_shouldBeBasedOnId() {
//        Notification n1 = new Notification(1L, "EMAIL", "a", "b");
//        Notification n2 = new Notification(1L, "SMS", "x", "y");
//
//        assertEquals(n1, n2);
//    }
//
//    @Test
//    void hashCode_shouldBeSameForSameId() {
//        Notification n1 = new Notification(1L, "EMAIL", "a", "b");
//        Notification n2 = new Notification(1L, "SMS", "x", "y");
//
//        assertEquals(n1.hashCode(), n2.hashCode());
//    }
//
//    @Test
//    void toString_shouldContainId() {
//        Notification n = new Notification(5L, "EMAIL", "user", "text");
//
//        assertTrue(n.toString().contains("5"));
//    }
//}
