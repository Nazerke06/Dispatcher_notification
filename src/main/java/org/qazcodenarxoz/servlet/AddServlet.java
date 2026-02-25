package org.qazcodenarxoz.servlet;

import jakarta.servlet.http.*;
import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.repository.NotificationRepository;
import java.io.IOException;

public class AddServlet extends HttpServlet {
    private final NotificationRepository<Notification> repo;
    private final Dispatcher<Notification> dispatcher;

    public AddServlet(NotificationRepository<Notification> repo, Dispatcher<Notification> dispatcher) {
        this.repo = repo;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String lang = getLanguage(req);
        String text = req.getParameter("message");
        String channel = req.getParameter("channel") != null ? req.getParameter("channel") : "SMS";

        if (text != null && !text.isEmpty()) {
            String prefixed = lang + " " + text;
            Notification n = new Notification(repo.nextId(), channel, "user@example.com", prefixed);
            repo.add(n);
            System.out.println("[UI] Добавлено из браузера: " + prefixed);
        }
        resp.sendRedirect("/ui");
    }

    private String getLanguage(HttpServletRequest req) {
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if ("lang".equals(c.getName())) return c.getValue();
            }
        }
        return "en";
    }
}