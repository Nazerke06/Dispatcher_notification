package org.qazcodenarxoz.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class EndpointLoggerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        System.out.println("\n=== REGISTERED SERVLET ENDPOINTS ===");
        context.getServletRegistrations().forEach((name, reg) ->
                System.out.println("Servlet: " + name + " → " + reg.getMappings())
        );

        System.out.println("\n=== REGISTERED FILTERS ===");
        context.getFilterRegistrations().forEach((name, reg) ->
                System.out.println("Filter: " + name + " → " + reg.getUrlPatternMappings())
        );

        System.out.println("=== END OF ENDPOINTS ===\n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // ничего не нужно
    }
}