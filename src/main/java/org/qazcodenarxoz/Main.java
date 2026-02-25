package org.qazcodenarxoz;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.qazcodenarxoz.console.ConsoleHandler;
import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.repository.NotificationRepository;
import org.qazcodenarxoz.util.SenderRegistry;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        NotificationRepository<Notification> repository = new NotificationRepository<>();
        SenderRegistry registry = new SenderRegistry("org.qazcodenarxoz.sender");
        Dispatcher<Notification> dispatcher = new Dispatcher<>(repository, registry);

        // Tomcat в отдельном потоке
        Thread tomcatThread = new Thread(() -> startEmbeddedTomcat(repository, dispatcher));
        tomcatThread.setDaemon(true);
        tomcatThread.start();

        System.out.println("=== HTTP сервер запущен: http://localhost:8080 ===");
        System.out.println("Консоль готова. Команды: add, gen, send, stats, queue, exit");

        ConsoleHandler console = new ConsoleHandler(repository, dispatcher);
        console.start();
    }

    private static void startEmbeddedTomcat(NotificationRepository<Notification> repo, Dispatcher<Notification> disp) {
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8080);
            tomcat.getConnector();

            String docBase = new File("target/classes/webapp").getAbsolutePath();
            Context ctx = tomcat.addWebapp("", docBase);

            // === Регистрируем всё программно (надёжно для embedded) ===
            // Language
            tomcat.addServlet(ctx, "LanguageServlet", new org.qazcodenarxoz.servlet.LanguageServlet());
            ctx.addServletMappingDecoded("/lang", "LanguageServlet");

            // UI
            tomcat.addServlet(ctx, "UiServlet", new org.qazcodenarxoz.servlet.UiServlet(repo, disp));
            ctx.addServletMappingDecoded("/ui", "UiServlet");

            // 5 ручек по заданию
            tomcat.addServlet(ctx, "AddServlet", new org.qazcodenarxoz.servlet.AddServlet(repo, disp));
            ctx.addServletMappingDecoded("/add", "AddServlet");

            tomcat.addServlet(ctx, "GenServlet", new org.qazcodenarxoz.servlet.GenServlet(repo, disp));
            ctx.addServletMappingDecoded("/gen", "GenServlet");

            tomcat.addServlet(ctx, "QueueServlet", new org.qazcodenarxoz.servlet.QueueServlet(repo));
            ctx.addServletMappingDecoded("/queue", "QueueServlet");

            tomcat.addServlet(ctx, "SendServlet", new org.qazcodenarxoz.servlet.SendServlet(disp));
            ctx.addServletMappingDecoded("/send", "SendServlet");

            tomcat.addServlet(ctx, "StatsServlet", new org.qazcodenarxoz.servlet.StatsServlet(disp));
            ctx.addServletMappingDecoded("/stats", "StatsServlet");

            // Logging Filter
            tomcat.addFilter(ctx, "LoggingFilter", new org.qazcodenarxoz.filter.LoggingFilter());
            ctx.addFilterMapping("LoggingFilter", "/*", false);

            tomcat.start();
            System.out.println("\n=== REGISTERED SERVLET ENDPOINTS ===");
            System.out.println("GET  /ui      → UiServlet");
            System.out.println("POST /add     → AddServlet");
            System.out.println("POST /gen     → GenServlet");
            System.out.println("GET  /queue   → QueueServlet");
            System.out.println("POST /send    → SendServlet");
            System.out.println("GET  /stats   → StatsServlet");
            System.out.println("GET  /lang    → LanguageServlet");
            System.out.println("Filter: LoggingFilter → /*");
            System.out.println("=== END OF ENDPOINTS ===\n");

            tomcat.getServer().await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}