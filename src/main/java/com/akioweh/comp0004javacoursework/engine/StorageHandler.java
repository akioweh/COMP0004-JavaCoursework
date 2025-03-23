package com.akioweh.comp0004javacoursework.engine;

import com.akioweh.comp0004javacoursework.models.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Handles the storage of notes and indexes.
 * This includes loading and saving dataclass objects
 * to and from the filesystem.
 * <br>
 * All objects are identified by their UUID.
 * (See UUIO abstract class)
 * <br>
 * Uses fasterxml Jackson for JSON serialization.
 * <p>
 * While this class is not enforced as a singleton,
 * the program uses it as such (see Engine).
 * </p>
 */
public class StorageHandler {
    private static final Logger logger = Logger.getLogger(StorageHandler.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    protected Path localStoragePath;
    protected Path localMediaStoragePath;

    public StorageHandler(String localStoragePath, String localMediaStoragePath) {
        // we check for path validity here to throw clearer errors if any
        this.localStoragePath = Path.of(localStoragePath);
        this.localMediaStoragePath = Path.of(localMediaStoragePath);
        // check path validity; create directories if they do not exist
        if (!this.localStoragePath.toFile().exists()) {
            logger.info("Local storage path does not exist, creating: " + this.localStoragePath);
            if (!this.localStoragePath.toFile().mkdirs()) {
                throw new RuntimeException("Failed to create local storage path: " + this.localStoragePath);
            }
        }
        if (!this.localMediaStoragePath.toFile().exists()) {
            logger.info("Local media storage path does not exist, creating: " + this.localMediaStoragePath);
            if (!this.localMediaStoragePath.toFile().mkdirs()) {
                throw new RuntimeException("Failed to create local media storage path: " + this.localMediaStoragePath);
            }
        }
    }

    public UUIO get(@NotNull UUID uuid) {
        String fileName = uuid + ".json";
        Path filePath = localStoragePath.resolve(fileName);
        if (!filePath.toFile().exists()) {
            logger.warning("Tried to get non-existent data file: " + filePath);
            return null;
        }
        try {
            return objectMapper.readValue(filePath.toFile(), UUIO.class);
        } catch (IOException e) {
            logger.severe("Failed to read data file: " + filePath);
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    public void save(@NotNull UUIO data) {
        String fileName = data.getUuid() + ".json";
        Path filePath = localStoragePath.resolve(fileName);
        try {
            objectMapper.writeValue(filePath.toFile(), data);
        } catch (IOException e) {
            logger.severe("Failed to write data file: " + filePath);
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void delete(@NotNull UUID uuid) {
        String fileName = uuid + ".json";
        Path filePath = localStoragePath.resolve(fileName);
        if (filePath.toFile().exists()) {
            if (!filePath.toFile().delete()) {
                logger.severe("Failed to delete data file: " + filePath);
            }
        } else {
            logger.warning("Tried to delete non-existent data file: " + filePath);
        }
    }

    public boolean exists(@NotNull UUID uuid) {
        String fileName = uuid + ".json";
        Path filePath = localStoragePath.resolve(fileName);
        return filePath.toFile().exists();
    }

    public Note getNote(@NotNull UUID uuid) {
        return (Note) get(uuid);
    }

    public void saveNote(@NotNull Note note) {
        save(note);
    }

    public void deleteNote(@NotNull UUID uuid) {
        delete(uuid);
    }

    public Index getIndex(@NotNull UUID uuid) {
        return (Index) get(uuid);
    }

    public void saveIndex(@NotNull Index index) {
        save(index);
    }

    public void deleteIndex(@NotNull UUID uuid) {
        delete(uuid);
    }
}
