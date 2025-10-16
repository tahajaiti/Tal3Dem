package org.kyojin.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.logging.Logger;

public class EnvLoader {

    private static final Logger logger = Logger.getLogger(EnvLoader.class.getName());

    private EnvLoader() {}

    public static void load() {
        Dotenv env = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        System.setProperty("DB_URL", env.get("DB_URL"));
        System.setProperty("DB_USER", env.get("DB_USER"));
        System.setProperty("DB_PASS", env.get("DB_PASS"));

        logger.info("Environment variables loaded successfully.");
    }
}
