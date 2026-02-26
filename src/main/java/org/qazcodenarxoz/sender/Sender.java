package org.qazcodenarxoz.sender;

import org.qazcodenarxoz.notification.Notification;

public interface Sender<T extends Notification> {
    void send(T notification) throws Exception;
}
