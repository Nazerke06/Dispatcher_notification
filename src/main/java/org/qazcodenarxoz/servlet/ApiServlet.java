package org.qazcodenarxoz.servlet;

import org.qazcodenarxoz.config.AppContext;
import org.qazcodenarxoz.dispatcher.Dispatcher;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.repository.NotificationRepository;
import org.qazcodenarxoz.util.IdGenerator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/*")
public class ApiServlet extends HttpServlet {

    private final Dispatcher<Notification> dispatcher =
            AppContext.getDispatcher();

    private final NotificationRepository<Notification> repo =
            AppContext.getRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String path = req.getPathInfo();

        if ("/queue".equals(path)) {
            resp.getWriter().write(
                    repo.getAll().stream().limit(20).toList().toString()
            );
        }

        if ("/stats".equals(path)) {
            resp.getWriter().write(
                    String.valueOf(dispatcher.getLastMetrics())
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String path = req.getPathInfo();

        if ("/gen".equals(path)) {
            int n = Integer.parseInt(req.getParameter("n"));
            for (int i = 0; i < n; i++) {
                repo.add(new Notification(
                        IdGenerator.nextId(),
                        "EMAIL",
                        "user",
                        "Generated"
                ));
            }
        }

        if ("/send".equals(path)) {
            int threads = Integer.parseInt(req.getParameter("threads"));
            try {
                dispatcher.sendAll(threads);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        resp.getWriter().write("OK");
    }
}
