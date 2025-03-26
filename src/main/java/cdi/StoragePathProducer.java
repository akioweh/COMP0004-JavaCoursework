package cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;


@ApplicationScoped
public class StoragePathProducer {
    private static final Logger logger = Logger.getLogger(StoragePathProducer.class.getName());
    /**
     * Storage paths.
     */
    private static final String LOCAL_STORAGE_ROOT = "data/";
    private static final String OBJECT_STORAGE_DIR = "objects/";
    private static final String MEDIA_STORAGE_DIR = "media/";
    @NotNull
    private final Path localStoragePath;
    @NotNull
    private final Path localMediaStoragePath;

    public StoragePathProducer() {
        final var rootPath = Paths.get(LOCAL_STORAGE_ROOT);
        this.localStoragePath = rootPath.resolve(OBJECT_STORAGE_DIR);
        this.localMediaStoragePath = rootPath.resolve(MEDIA_STORAGE_DIR);
        // Create directories if they do not exist
        try {
            if (!Files.exists(rootPath)) {
                logger.info("Creating root storage directory: " + rootPath);
                Files.createDirectories(rootPath);
            }
            if (!Files.exists(localStoragePath)) {
                logger.info("Creating local storage directory: " + localStoragePath);
                Files.createDirectory(localStoragePath);
            }
            if (!Files.exists(localMediaStoragePath)) {
                logger.info("Creating local media storage directory: " + localMediaStoragePath);
                Files.createDirectory(localMediaStoragePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directories", e);
        }
    }

    @Produces
    @Named("localStoragePath")
    public @NotNull Path getLocalStoragePath() {
        return localStoragePath;
    }

    @Produces
    @Named("localMediaStoragePath")
    public @NotNull Path getLocalMediaStoragePath() {
        return localMediaStoragePath;
    }
}
