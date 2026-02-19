package org.qazcodenarxoz.util;

import lombok.extern.slf4j.Slf4j;
import org.qazcodenarxoz.annotation.ChannelHandler;
import org.qazcodenarxoz.sender.EmailSender;
import org.qazcodenarxoz.sender.SMSSender;
import org.qazcodenarxoz.sender.Sender;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SenderRegistry {

    private final Map<String, Sender<?>> senders = new HashMap<>();

    public SenderRegistry(String packageName) {
        autoDiscover(packageName);
    }

    private void autoDiscover(String packageName) {
        try {
            Reflections reflections = new Reflections(packageName, Scanners.TypesAnnotated);
            var classes = reflections.getTypesAnnotatedWith(ChannelHandler.class);

            for (Class<?> clazz : classes) {
                register(clazz);
            }
        } catch (Exception e) {
            log.warn("Reflection error: {}", e.getMessage(), e);
        }
    }

    private void register(Class<?> clazz) {
        try {
            ChannelHandler annotation = clazz.getAnnotation(ChannelHandler.class);
            Sender<?> sender = (Sender<?>) clazz.getDeclaredConstructor().newInstance();
            senders.put(annotation.value(), sender);
            log.info("Plugin loaded: {}", annotation.value());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<Sender<?>> getSender(String channel) {
        return Optional.ofNullable(senders.get(channel));
    }
}

