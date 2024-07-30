package com.ezeeinfo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.sql.SQLException;


/**
 * The type Application.
 */
@SpringBootApplication
public class EzeeInfoApplication {
    /**
     * Logger.
     */
    private final Logger logger =
            LoggerFactory.getLogger(EzeeInfoApplication.class);

    /**
     * Main method of this application.
     *
     * @param args the args
     */
    public static void main(final String[] args) {
        SpringApplication.run(EzeeInfoApplication.class, args);
    }

    /**
     * This will be invoked one the application is started.
     *
     * @param event
     */
    @EventListener
    public void onApplicationEvent(final ContextRefreshedEvent event)
            throws SQLException, IOException {
        logger.info("Application Started at {}", event.getTimestamp());
    }
}
