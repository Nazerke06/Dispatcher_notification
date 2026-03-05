package org.qazcodenarxoz.servlet;

import org.qazcodenarxoz.config.AppContext;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.util.IdGenerator;
import org.qazcodenarxoz.web.FlashMessage;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet("/gen")
public class GenServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + "/ui");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String countParam = req.getParameter("count");
        HttpSession session = req.getSession();

        try {
            int count = Integer.parseInt(countParam);
            if (count <= 0 || count > 1000) {
                session.setAttribute("flash", new FlashMessage("error", "Count must be between 1 and 1000"));
                resp.sendRedirect("/ui");
                return;
            }

            String[] channels = {"EMAIL", "SMS", "WHATSAPP"};
            ThreadLocalRandom random = ThreadLocalRandom.current();

            for (int i = 0; i < count; i++) {
                long id = IdGenerator.nextId();
                String channel = channels[random.nextInt(channels.length)];
                Notification n = new Notification(id, channel, "user_" + id, "Generated from UI #" + id);
                AppContext.getRepository().add(n);
            }

            session.setAttribute("flash", new FlashMessage("success", count + " notifications generated"));
        } catch (NumberFormatException e) {
            session.setAttribute("flash", new FlashMessage("error", "Invalid number"));
        }

        resp.sendRedirect("/ui");
    }
}
