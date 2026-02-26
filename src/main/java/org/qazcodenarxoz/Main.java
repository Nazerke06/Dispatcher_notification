package org.qazcodenarxoz;

import lombok.extern.slf4j.Slf4j;
import org.qazcodenarxoz.config.AppContext;
import org.qazcodenarxoz.console.ConsoleHandler;
import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.repository.NotificationRepository;

@Slf4j
public class Main {

    public static void main(String[] args) {

        NotificationRepository<Notification> repo =
                AppContext.getRepository();

        Dispatcher<Notification> dispatcher =
                AppContext.getDispatcher();

        System.out.println("Dispatcher started. Type 'exit' to quit.");

        ConsoleHandler console =
                new ConsoleHandler(repo, dispatcher);

        console.start();
    }
}