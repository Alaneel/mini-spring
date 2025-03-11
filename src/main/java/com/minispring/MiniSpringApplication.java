package com.minispring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minispring.context.AnnotationConfigApplicationContext;
import com.minispring.context.ApplicationContext;

/**
 * Entry point for a Mini Spring application.
 * This class bootstraps a Mini Spring application from the specified main class.
 */
public class MiniSpringApplication {

    private static final Logger logger = LoggerFactory.getLogger(MiniSpringApplication.class);

    /**
     * Run a Mini Spring application, creating and refreshing a new ApplicationContext.
     *
     * @param primarySource the primary source class for the application
     * @param args the application arguments
     * @return the running ApplicationContext
     */
    public static ApplicationContext run(Class<?> primarySource, String... args) {
        logger.info("Starting Mini Spring Application");
        return doRun(primarySource, args);
    }

    private static ApplicationContext doRun(Class<?> primarySource, String... args) {
        // Create and configure the ApplicationContext
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(primarySource);

        logger.info("Mini Spring Application started");
        return context;
    }
}