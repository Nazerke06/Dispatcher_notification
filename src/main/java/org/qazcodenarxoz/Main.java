package org.qazcodenarxoz;

import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.notification.OTPNotification;
import org.qazcodenarxoz.repository.NotificationRepository;
import org.qazcodenarxoz.util.SenderRegistry;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Main {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public static void main(String[] args) {
        NotificationRepository<Notification> repository = new NotificationRepository<>();
        SenderRegistry registry = new SenderRegistry("org.qazcodenarxoz.util");
        Dispatcher<Notification> dispatcher = new Dispatcher<>(repository, registry);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Dispatcher started. Type 'exit' to quit.");

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "add" -> handleAdd(parts, line, repository);
                    case "gen" -> handleGen(parts, repository);
                    case "list" -> handleList(repository);
                    case "send" -> handleSend(parts, dispatcher);
                    case "stats" -> dispatcher.printStats();
                    case "exit" -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Unknown command: " + command);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void handleAdd(String[] parts, String fullLine, NotificationRepository<Notification> repo) {
        if (parts.length < 4) {
            System.out.println("Usage: add <CHANNEL> <TO> <TEXT...>");
            return;
        }
        String channel = parts[1].toUpperCase();
        String to = parts[2];
        String text = fullLine.substring(fullLine.indexOf(to) + to.length()).trim();

        Notification n;
        if (text.contains("OTP")) {
            n = new OTPNotification(ID_GENERATOR.getAndIncrement(), channel, to, text, "1234");
        } else {
            n = new Notification(ID_GENERATOR.getAndIncrement(), channel, to, text);
        }

        repo.add(n);
        System.out.println("Added: " + n);
    }

    private static void handleGen(String[] parts, NotificationRepository<Notification> repo) {
        if (parts.length < 2) return;
        int n = Integer.parseInt(parts[1]);
        String[] channels = {"EMAIL", "SMS", "WHATSAPP"};
        for (int i = 0; i < n; i++) {
            String ch = channels[ThreadLocalRandom.current().nextInt(channels.length)];
            repo.add(new Notification(ID_GENERATOR.getAndIncrement(), ch, "user" + i, "Msg " + i));
        }
        System.out.println("Generated " + n + " items.");
    }

    private static void handleList(NotificationRepository<Notification> repo) {
        repo.getAll().stream()
                .limit(20)
                .forEach(System.out::println);
    }

    private static void handleSend(String[] parts, Dispatcher<Notification> dispatcher) throws InterruptedException {
        if (parts.length < 2) return;
        int threads = Integer.parseInt(parts[1]);
        dispatcher.sendAll(threads);
    }
}