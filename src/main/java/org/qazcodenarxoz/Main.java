package org.qazcodenarxoz;

import lombok.extern.slf4j.Slf4j;
import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.repository.NotificationRepository;
import org.qazcodenarxoz.util.SenderRegistry;
import org.qazcodenarxoz.console.ConsoleHandler;



@Slf4j
public class Main {
    public static void main(String[] args) {
        NotificationRepository<Notification> repository = new NotificationRepository<>();
        SenderRegistry registry = new SenderRegistry("org.qazcodenarxoz.sender");
        Dispatcher<Notification> dispatcher = new Dispatcher<>(repository, registry);

        ConsoleHandler handler = new ConsoleHandler(repository, dispatcher);

        System.out.println("Dispatcher started. Type 'exit' to quit.");
        handler.start();
    }
}