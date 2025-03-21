package com.akioweh.comp0004javacoursework.main;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


@SuppressWarnings("CallToPrintStackTrace")
public class Main {
    private static final int DEFAULT_PORT = 80;
    private static final String DEFAULT_WEBAPP_DIR = "src/main/webapp/";
    private static final String DEFAULT_TARGET_CLASSES = "target/classes";
    private static final String WEB_INF_CLASSES = "/WEB-INF/classes";
    private static final String LOGFILE = "logfile.txt";

    private static @NotNull Logger initLogger() {
        Logger logger = Logger.getLogger(Main.class.getName());
        try {
            FileHandler fileHandler = new FileHandler(LOGFILE);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.INFO);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create log file", e);
        }
        logger.setLevel(Level.INFO);
        return logger;
    }

    @Contract("_, _ -> new")
    private static @NotNull Thread shutdownHook(final Tomcat app, final Logger logger) {
        return new Thread(() -> {
            try {
                app.stop();
                app.destroy();
                logger.info("Server closed.");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error closing server ", e);
                e.printStackTrace();
            }
        });
    }

    // run with embedded Tomcat
    public static void main(String[] args) {
        final Logger logger = initLogger();
        final Path appDirectory = Paths.get(DEFAULT_WEBAPP_DIR);
        final Path targetClassesDirectory = Paths.get(DEFAULT_TARGET_CLASSES);

        assert Files.exists(appDirectory) : "Webapp directory does not exist: " + appDirectory;
        assert Files.exists(targetClassesDirectory) : "Target classes directory does not exist: " + targetClassesDirectory;

        final Tomcat tomcat = new Tomcat();

        tomcat.setPort(DEFAULT_PORT);
        tomcat.getConnector();
        // add a shutdown hook to *eliminate* Tomcat when the JVM exits
        Runtime.getRuntime().addShutdownHook(shutdownHook(tomcat, logger));

        // tell it where to find the webapp
        Context context = tomcat.addWebapp("", appDirectory.toAbsolutePath().toString());
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, WEB_INF_CLASSES,
                targetClassesDirectory.toAbsolutePath().toString(), "/"));
        context.setResources(resources);

        try {
            tomcat.start();
            logger.info("Server started on port " + DEFAULT_PORT);
            tomcat.getServer().await();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error starting server ", e);
            e.printStackTrace();
        }
    }
}
