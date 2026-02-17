package org.qazcodenarxoz.util;

import org.qazcodenarxoz.annotation.ChannelHandler;
import org.qazcodenarxoz.sender.EmailSender;
import org.qazcodenarxoz.sender.SMSSender;
import org.qazcodenarxoz.sender.Sender;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SenderRegistry {

    private final Map<String, Sender<?>> senders = new HashMap<>();

    public SenderRegistry() {
        register(EmailSender.class);
        register(SMSSender.class);
    }

    private void register(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ChannelHandler.class)) {
            ChannelHandler annotation = clazz.getAnnotation(ChannelHandler.class);
            try {
                Sender<?> sender =
                        (Sender<?>) clazz.getDeclaredConstructor().newInstance();
                senders.put(annotation.value(), sender);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Optional<Sender<?>> getSender(String channel) {
        return Optional.ofNullable(senders.get(channel));
    }
}

