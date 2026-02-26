package org.qazcodenarxoz.servlet;

import org.qazcodenarxoz.config.AppContext;
import org.qazcodenarxoz.dispatcher.DispatchStrategy;
import org.qazcodenarxoz.dispatcher.strategy.CachedThreadPoolStrategy;
import org.qazcodenarxoz.dispatcher.strategy.FixedThreadPoolStrategy;
import org.qazcodenarxoz.dispatcher.strategy.SingleThreadStrategy;
import org.qazcodenarxoz.web.FlashMessage;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/send")
public class SendServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + "/ui");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String strategyParam = req.getParameter("strategy");
        String threadsParam = req.getParameter("threads");
        HttpSession session = req.getSession();

        try {
            DispatchStrategy strategy;
            if ("cached".equalsIgnoreCase(strategyParam)) {
                strategy = new CachedThreadPoolStrategy();
            } else if ("single".equalsIgnoreCase(strategyParam)) {
                strategy = new SingleThreadStrategy();
            } else { // по умолчанию fixed
                int threads = threadsParam != null ? Integer.parseInt(threadsParam) : 5;
                if (threads <= 0) {
                    session.setAttribute("flash", new FlashMessage("error", "Threads must be positive"));
                    resp.sendRedirect("/ui");
                    return;
                }
                strategy = new FixedThreadPoolStrategy(threads);
            }

            AppContext.getDispatcher().sendAll(strategy);
            session.setAttribute("flash", new FlashMessage("success", "Dispatch completed with strategy: " + strategyParam));
        } catch (NumberFormatException e) {
            session.setAttribute("flash", new FlashMessage("error", "Invalid threads number"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            session.setAttribute("flash", new FlashMessage("error", "Dispatch interrupted"));
        }

        resp.sendRedirect("/ui");
    }
}