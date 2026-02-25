package org.qazcodenarxoz.servlet;


import org.qazcodenarxoz.console.MessageProvider;
import org.qazcodenarxoz.service.Dispatcher;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet("/dispatch")
public class DispatchServlet extends HttpServlet {

    private Dispatcher dispatcher;

    @Override
    public void init() {
        dispatcher = new Dispatcher();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String lang = getLanguage(req);
        String message = MessageProvider.getMessage("welcome", lang);

        req.setAttribute("message", message);

        req.getRequestDispatcher("/WEB-INF/jsp/dispatch.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String msg = req.getParameter("message");
        if (msg != null && !msg.isEmpty()) {
            dispatcher.dispatch(msg);
        }
        resp.sendRedirect("/dispatch");
    }

    private String getLanguage(HttpServletRequest req) {
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("lang".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return "en";
    }

    @Override
    public void destroy() {
        dispatcher.shutdown();
    }
}
