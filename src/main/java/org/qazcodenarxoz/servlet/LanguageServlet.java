package org.qazcodenarxoz.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/lang")
public class LanguageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String lang = req.getParameter("lang");

        if (lang != null) {
            Cookie cookie = new Cookie("lang", lang);
            cookie.setMaxAge(60 * 60 * 24 * 30);
            cookie.setPath("/");
            resp.addCookie(cookie);
        }

        // Используем contextPath, чтобы редирект работал в любом случае
        resp.sendRedirect(req.getContextPath() + "/dispatch");
    }
}
