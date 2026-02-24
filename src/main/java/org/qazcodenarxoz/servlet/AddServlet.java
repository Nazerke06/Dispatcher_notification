package org.qazcodenarxoz.servlet;

import org.qazcodenarxoz.config.AppContext;
import org.qazcodenarxoz.notification.Notification;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/add")
public class AddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String lang = getLang(req);
        String text = req.getParameter("message");

        if (text != null && !text.isBlank()) {
            long id = System.currentTimeMillis();

            Notification n = new Notification(
                    id,
                    "EMAIL",
                    "web_user",
                    lang + " " + text   // 🔥 язык добавляем в текст
            );

            AppContext.getRepository().add(n);
        }

        resp.sendRedirect("/ui");
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