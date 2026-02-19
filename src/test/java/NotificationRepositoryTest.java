
import org.junit.jupiter.api.Test;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.repository.NotificationRepository;


import static org.junit.jupiter.api.Assertions.*;

class NotificationRepositoryTest {

    @Test
    void add_and_poll_shouldWorkCorrectly() {
        NotificationRepository<Notification> repo = new NotificationRepository<>();

        Notification n = new Notification(1L, "EMAIL", "user", "text");
        repo.add(n);

        assertFalse(repo.isEmpty());

        Notification polled = repo.poll();

        assertEquals(n, polled);
        assertTrue(repo.isEmpty());
    }

    @Test
    void getAll_shouldReturnCopy() {
        NotificationRepository<Notification> repo = new NotificationRepository<>();

        repo.add(new Notification(1L, "EMAIL", "user", "text"));

        var list = repo.getAll();
        list.clear();

        assertFalse(repo.isEmpty()); // оригинальная очередь не изменилась
    }
}
