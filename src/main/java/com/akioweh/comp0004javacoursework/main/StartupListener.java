package com.akioweh.comp0004javacoursework.main;

import com.akioweh.comp0004javacoursework.engine.Engine;
import com.akioweh.comp0004javacoursework.models.Index;
import jakarta.servlet.ServletContextListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;


/**
 * This does the one job of initializing the storage engine
 */
public class StartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(jakarta.servlet.ServletContextEvent sce) {
        final String localStorageRoot = sce.getServletContext().getInitParameter("localStorageRoot");
        if (localStorageRoot == null) {
            throw new RuntimeException("LOCAL_STORAGE_ROOT not set in web.xml");
        }

        var logger = Logger.getLogger(Main.class.getName());
        logger.info("Initializing storage...");
        var rootPath = Paths.get(localStorageRoot);
        if (!rootPath.toFile().exists()) {
            try {
                Files.createDirectories(rootPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create local storage root directory: " + rootPath, e);
            }
        }
        var rootIndexPath = rootPath.resolve("rootIndex.id");
        if (!rootIndexPath.toFile().exists()) {
            // doesn't exist; initialize the root index
            try {
                logger.info("Did not find root index pointer; assuming empty storage. Initializing...");
                Files.createFile(rootIndexPath);
                var rootIndex = new Index("Root", "Collection of all existing notes and indexes");
                var rootIndexUuid = rootIndex.getUuid();
                Files.writeString(rootIndexPath, rootIndexUuid.toString());
                Engine.init(localStorageRoot, localStorageRoot, rootIndexUuid);
                Engine.getInstance().addIndex(rootIndex);
                Engine.getInstance().saveAll();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create root index file: " + rootIndexPath, e);
            }
        } else {
            // all good; read file to determine root and initialize engine
            try {
                var rootIndexUuid = UUID.fromString(Files.readString(rootIndexPath));
                Engine.init(localStorageRoot, localStorageRoot, rootIndexUuid);
            } catch (IOException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to determine root index UUID from " + rootIndexPath, e);
            }
        }
    }

    @Override
    public void contextDestroyed(jakarta.servlet.ServletContextEvent sce) {
        Engine.getInstance().saveAll();
    }
}
