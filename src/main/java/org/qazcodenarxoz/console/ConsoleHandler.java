package org.qazcodenarxoz.console;

import lombok.RequiredArgsConstructor;
import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.notification.OTPNotification;
import org.qazcodenarxoz.repository.NotificationRepository;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;


public class ConsoleHandler {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private final NotificationRepository<Notification> repository;
    private final Dispatcher<Notification> dispatcher;
    private final Scanner scanner;

    public ConsoleHandler(NotificationRepository<Notification> repository, Dispatcher<Notification> dispatcher) {
        this.repository = repository;
        this.dispatcher = dispatcher;
        this.scanner = new Scanner(System.in);
    }


    public void start() {
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

    private void handleAdd(String[] parts, String fullLine, NotificationRepository<Notification> repo) {
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

    private void handleGen(String[] parts, NotificationRepository<Notification> repo) {
        try {
            int n = Integer.parseInt(parts[1]);
            String[] channels = {"EMAIL", "SMS", "WHATSAPP"};
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int i = 0; i < n; i++) {
                long id = ID_GENERATOR.getAndIncrement();
                String ch = channels[random.nextInt(channels.length)];
                repo.add(new Notification(id, ch, "user_" + id, "Test content #" + id));
            }
            System.out.printf("Generated: %d items%n", n);
        } catch (Exception e) {
            System.out.println("Error in generator: " + e.getMessage());
        }
    }

    private void handleList(NotificationRepository<Notification> repo) {
        repo.getAll().stream().limit(20).forEach(System.out::println);
    }

    private void handleSend(String[] parts, Dispatcher<Notification> dispatcher) throws InterruptedException {
        if (parts.length < 2) return;
        int threads = Integer.parseInt(parts[1]);
        dispatcher.sendAll(threads);
    }
}
