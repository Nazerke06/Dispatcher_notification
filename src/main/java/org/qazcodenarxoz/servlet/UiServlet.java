package org.qazcodenarxoz.servlet;

import org.qazcodenarxoz.config.AppContext;
import org.qazcodenarxoz.web.FlashMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/ui")
public class UiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null) {
            FlashMessage flash = (FlashMessage) session.getAttribute("flash");
            if (flash != null) {
                req.setAttribute("flash", flash);
                session.removeAttribute("flash"); // удаляем после прочтения
            }
        }

        String lang = getLang(req);
        req.setAttribute("lang", lang);
        req.setAttribute("queue",
                AppContext.getRepository().getAll().stream().limit(20).toList());
        req.setAttribute("stats",
                AppContext.getDispatcher().getLastMetrics());

        req.getRequestDispatcher("/WEB-INF/jsp/ui.jsp").forward(req, resp);
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
