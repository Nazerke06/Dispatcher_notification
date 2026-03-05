package org.qazcodenarxoz.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

    // JPA
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("default");

    public static NotificationRepository<Notification> getRepository() {
        return repository;
    }

    public static Dispatcher<Notification> getDispatcher() {
        return dispatcher;
    }

    public static EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

//public class AppContext {
//
//    private static final NotificationRepository<Notification> repository =
//            new NotificationRepository<>();
//
//    private static final SenderRegistry registry =
//            new SenderRegistry("org.qazcodenarxoz.sender");
//
//    private static final Dispatcher<Notification> dispatcher =
//            new Dispatcher<>(repository, registry);
//
//    public static NotificationRepository<Notification> getRepository() {
//        return repository;
//    }
//
//    public static Dispatcher<Notification> getDispatcher() {
//        return dispatcher;
//    }
//}
