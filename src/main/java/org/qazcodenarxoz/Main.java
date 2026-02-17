package org.qazcodenarxoz;

import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.repository.NotificationRepository;
import org.qazcodenarxoz.util.SenderRegistry;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Main {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public static void main(String[] args) throws Exception {

        NotificationRepository<Notification> repository = new NotificationRepository<>();
        SenderRegistry registry = new SenderRegistry();
        Dispatcher<Notification> dispatcher = new Dispatcher<>(repository, registry);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Dispatcher started. Enter command:");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();

            if (line.isBlank()) continue;

            String[] parts = line.split(" ");
            String command = parts[0].toLowerCase();

            switch (command) {

                case "add" -> {
                    if (parts.length < 4) {
                        System.out.println("Usage: add <CHANNEL> <TO> <TEXT>");
                        continue;
                    }

                    String channel = parts[1];
                    String to = parts[2];
                    String text = line.substring(line.indexOf(to) + to.length() + 1);

                    Notification notification = new Notification(
                            ID_GENERATOR.getAndIncrement(),
                            channel,
                            to,
                            text
                    );

                    repository.add(notification);
                    System.out.println("Added: " + notification);
                }

                case "gen" -> {
                    if (parts.length != 2) {
                        System.out.println("Usage: gen <N>");
                        continue;
                    }

                    int n = Integer.parseInt(parts[1]);
                    List<String> channels = List.of("EMAIL", "SMS");

                    for (int i = 0; i < n; i++) {
                        String channel = channels.get(
                                ThreadLocalRandom.current().nextInt(channels.size())
                        );

                        Notification notification = new Notification(
                                ID_GENERATOR.getAndIncrement(),
                                channel,
                                "user" + i + "@mail.com",
                                "Test message " + i
                        );

                        repository.add(notification);
                    }

                    System.out.println("Generated " + n + " notifications.");
                }

                case "list" -> {
                    List<Notification> first20 = repository.getAll()
                            .stream()
                            .limit(20)
                            .collect(Collectors.toList());

                    first20.forEach(System.out::println);
                    System.out.println("Total in queue: " + repository.getAll().size());
                }

                case "send" -> {
                    if (parts.length != 2) {
                        System.out.println("Usage: send <THREADS>");
                        continue;
                    }

                    int threads = Integer.parseInt(parts[1]);
                    dispatcher.sendAll(threads);
                    System.out.println("Sending finished.");
                }

                case "stats" -> dispatcher.printStats();

                case "exit" -> {
                    System.out.println("Bye.");
                    return;
                }

                default -> System.out.println("Unknown command");
            }
        }
    }
}
