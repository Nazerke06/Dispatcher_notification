package org.qazcodenarxoz.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.flywaydb.core.Flyway;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Run Flyway migrations
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:mariadb://localhost:3306/transactiondb?useSSL=false",
                        "app",
                        "app123")
                .load();
        flyway.migrate();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        AppContext.closeEntityManagerFactory();
    }
}
