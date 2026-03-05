package org.qazcodenarxoz.servlet;

import org.qazcodenarxoz.config.AppContext;
import org.qazcodenarxoz.notification.Notification;
import org.qazcodenarxoz.notification.OTPNotification;
import org.qazcodenarxoz.util.IdGenerator;
import org.qazcodenarxoz.web.FlashMessage;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet("/add")
public class AddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String lang = getLang(req);
        String channel = req.getParameter("channel");
        String to = req.getParameter("to");
        String text = req.getParameter("message");

        HttpSession session = req.getSession();

        if (channel == null || to == null || text == null || text.isBlank()) {
            session.setAttribute("flash", new FlashMessage("error", "All fields are required"));
            resp.sendRedirect(req.getContextPath() + "/ui");
            return;
        }

        try {
            long id = IdGenerator.nextId();
            Notification notification;
            if (text.contains("OTP")) {
                String code = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
                notification = new OTPNotification(id, channel.toUpperCase(), to, text, code);
            } else {
                notification = new Notification(id, channel.toUpperCase(), to, text);
            }

            AppContext.getRepository().add(notification);
            session.setAttribute("flash", new FlashMessage("success", "Notification added"));
        } catch (Exception e) {
            session.setAttribute("flash", new FlashMessage("error", "Error: " + e.getMessage()));
        }

        resp.sendRedirect(req.getContextPath() + "/ui");
    }

    private String getLang(HttpServletRequest req) {
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if ("lang".equals(c.getName())) return c.getValue();
            }
        }
        return "en";
    }
}
