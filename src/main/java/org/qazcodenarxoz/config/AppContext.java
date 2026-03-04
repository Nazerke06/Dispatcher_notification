package org.qazcodenarxoz.config;

import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.repository.NotificationRepository;
import org.qazcodenarxoz.util.SenderRegistry;

public class AppContext {

    private static final NotificationRepository<Notification> repository =
            new NotificationRepository<>();

    private static final SenderRegistry registry =
            new SenderRegistry("org.qazcodenarxoz.sender");

    private static final Dispatcher<Notification> dispatcher =
            new Dispatcher<>(repository, registry);

    public static NotificationRepository<Notification> getRepository() {
        return repository;
    }

    public static Dispatcher<Notification> getDispatcher() {
        return dispatcher;
    }
}