package org.qazcodenarxoz.servlet;

import org.qazcodenarxoz.config.AppContext;
import org.qazcodenarxoz.notification.Notification;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/gen")
public class GenServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int count = Integer.parseInt(req.getParameter("count"));

        for (int i = 0; i < count; i++) {
            long id = System.nanoTime();
            AppContext.getRepository().add(
                    new Notification(id, "EMAIL", "web_user",
                            "Generated from UI #" + id)
            );
        }

        resp.sendRedirect("/ui");
    }
}