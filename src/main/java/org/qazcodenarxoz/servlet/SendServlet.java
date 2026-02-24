package org.qazcodenarxoz.servlet;

import org.qazcodenarxoz.config.AppContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/send")
public class SendServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int threads = Integer.parseInt(req.getParameter("threads"));

        try {
            AppContext.getDispatcher().sendAll(threads);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        resp.sendRedirect("/ui");
    }
}