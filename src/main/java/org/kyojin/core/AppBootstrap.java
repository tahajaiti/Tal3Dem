package org.kyojin.core;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AppBootstrap implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Injector.excludePackage(List.of("org.kyojin.controller", "org.kyojin.core"));
        Injector.scanPackage("org.kyojin");

        logger.info("Application context initialized and packages scanned.");
    }
}
