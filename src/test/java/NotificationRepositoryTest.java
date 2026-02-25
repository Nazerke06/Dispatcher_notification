import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.repository.NotificationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationRepositoryTest {

    @Test
    @DisplayName("add() и poll() должны корректно добавлять и извлекать элемент")
    void addAndPoll_shouldWorkCorrectly() {
        NotificationRepository<Notification> repository = new NotificationRepository<>();

        Notification notification = new Notification(1L, "EMAIL", "user", "text");
        repository.add(notification);

        assertFalse(repository.isEmpty(), "Очередь не должна быть пустой после добавления");

        Notification polled = repository.poll();

        assertEquals(notification, polled, "Извлечённый элемент должен совпадать с добавленным");
        assertTrue(repository.isEmpty(), "Очередь должна быть пустой после извлечения");
    }

    @Test
    @DisplayName("poll() на пустой очереди должен возвращать null")
    void poll_onEmptyQueue_shouldReturnNull() {
        NotificationRepository<Notification> repository = new NotificationRepository<>();

        assertNull(repository.poll(), "poll() должен вернуть null для пустой очереди");
    }

    @Test
    @DisplayName("getAll() должен возвращать копию, а не оригинальную очередь")
    void getAll_shouldReturnCopy() {
        NotificationRepository<Notification> repository = new NotificationRepository<>();

        repository.add(new Notification(1L, "EMAIL", "user", "text"));

        List<Notification> list = repository.getAll();
        assertThrows(UnsupportedOperationException.class, list::clear);

        assertFalse(repository.isEmpty(),
                "Оригинальная очередь не должна измениться после изменения возвращённого списка");
    }

    @Test
    @DisplayName("getAll() должен возвращать все элементы")
    void getAll_shouldReturnAllElements() {
        NotificationRepository<Notification> repository = new NotificationRepository<>();

        repository.add(new Notification(1L, "EMAIL", "user1", "text1"));
        repository.add(new Notification(2L, "SMS", "user2", "text2"));

        List<Notification> all = repository.getAll();

        assertEquals(2, all.size(), "Список должен содержать 2 элемента");
    }
}