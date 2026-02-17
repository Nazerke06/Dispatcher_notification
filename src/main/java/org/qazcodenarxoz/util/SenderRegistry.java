package org.qazcodenarxoz.util;

import org.qazcodenarxoz.annotation.ChannelHandler;
import org.qazcodenarxoz.sender.EmailSender;
import org.qazcodenarxoz.sender.SMSSender;
import org.qazcodenarxoz.sender.Sender;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SenderRegistry {

    private final Map<String, Sender<?>> senders = new HashMap<>();

    public SenderRegistry(String packageName) {
        autoDiscover(packageName);
    }

    private void autoDiscover(String packageName) {
        try {
            String path = packageName.replace('.', '/');
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            var resource = loader.getResource(path);

            if (resource == null) {
                System.err.println("Package path not found: " + path);
                return;
            }

            // Используем toURI(), чтобы корректно обработать пробелы и спецсимволы в путях
            File directory = new File(resource.toURI());

            if (!directory.exists()) return;

            File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));

            if (files != null) {
                for (File file : files) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    Class<?> clazz = Class.forName(className);

                    if (clazz.isAnnotationPresent(ChannelHandler.class)) {
                        register(clazz);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Reflection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void register(Class<?> clazz) {
        try {
            ChannelHandler annotation = clazz.getAnnotation(ChannelHandler.class);
            Sender<?> sender = (Sender<?>) clazz.getDeclaredConstructor().newInstance();
            senders.put(annotation.value(), sender);
            System.out.println("Plugin loaded: " + annotation.value());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<Sender<?>> getSender(String channel) {
        return Optional.ofNullable(senders.get(channel));
    }
}

