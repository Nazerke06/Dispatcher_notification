package org.qazcodenarxoz.servlet;

import org.qazcodenarxoz.config.AppContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ui")
public class UiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String lang = getLang(req);

        req.setAttribute("lang", lang);
        req.setAttribute("queue",
                AppContext.getRepository().getAll().stream().limit(20).toList());
        req.setAttribute("stats",
                AppContext.getDispatcher().getLastMetrics());

        req.getRequestDispatcher("/WEB-INF/jsp/ui.jsp")
                .forward(req, resp);
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