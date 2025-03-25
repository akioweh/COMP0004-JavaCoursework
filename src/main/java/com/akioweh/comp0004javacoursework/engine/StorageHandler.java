package com.akioweh.comp0004javacoursework.engine;

import com.akioweh.comp0004javacoursework.models.Index;
import com.akioweh.comp0004javacoursework.models.Note;
import com.akioweh.comp0004javacoursework.models.UUIO;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
 * Uses standard Java serialization for object persistence.
 * <p>
 * While this class is not enforced as a singleton,
 * the program uses it as such (see Engine).
 * </p>
 */
public class StorageHandler {
    private static final Logger logger = Logger.getLogger(StorageHandler.class.getName());
    protected Path localStoragePath;
    protected Path localMediaStoragePath;

    /**
     * Constructor for StorageHandler.
     * 
     * @param localStoragePath Path to store serialized objects
     * @param localMediaStoragePath Path to store media files
     */
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

    /**
     * Reads a UUIO object from disk.
     * 
     * @param uuid UUID of the object to read
     * @return The object, or null if not found or error
     */
    public UUIO read(@NotNull UUID uuid) {
        String fileName = uuid + ".ser";
        Path filePath = localStoragePath.resolve(fileName);
        if (!filePath.toFile().exists()) {
            logger.warning("Tried to get non-existent data file: " + filePath);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (UUIO) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Failed to read data file: " + filePath);
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Writes a UUIO object to disk.
     * 
     * @param data The object to write
     */
    public void write(@NotNull UUIO data) {
        String fileName = data.getUuid() + ".ser";
        Path filePath = localStoragePath.resolve(fileName);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            logger.severe("Failed to write data file: " + filePath);
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Deletes a UUIO object from disk.
     * 
     * @param uuid UUID of the object to delete
     */
    public void delete(@NotNull UUID uuid) {
        String fileName = uuid + ".ser";
        Path filePath = localStoragePath.resolve(fileName);
        if (filePath.toFile().exists()) {
            if (!filePath.toFile().delete()) {
                logger.severe("Failed to delete data file: " + filePath);
            }
        } else {
            logger.warning("Tried to delete non-existent data file: " + filePath);
        }
    }

    /**
     * Checks if a UUIO object exists on disk.
     * 
     * @param uuid UUID of the object to check
     * @return true if the object exists, false otherwise
     */
    public boolean exists(@NotNull UUID uuid) {
        String fileName = uuid + ".ser";
        Path filePath = localStoragePath.resolve(fileName);
        return filePath.toFile().exists();
    }

    /**
     * Reads a Note object from disk.
     * 
     * @param uuid UUID of the note to read
     * @return The note, or null if not found or error
     */
    public Note readNote(@NotNull UUID uuid) {
        return (Note) read(uuid);
    }

    /**
     * Writes a Note object to disk.
     * 
     * @param note The note to write
     */
    public void writeNote(@NotNull Note note) {
        write(note);
    }

    /**
     * Deletes a Note object from disk.
     * 
     * @param uuid UUID of the note to delete
     */
    public void deleteNote(@NotNull UUID uuid) {
        delete(uuid);
    }

    /**
     * Reads an Index object from disk.
     * 
     * @param uuid UUID of the index to read
     * @return The index, or null if not found or error
     */
    public Index readIndex(@NotNull UUID uuid) {
        return (Index) read(uuid);
    }

    /**
     * Writes an Index object to disk.
     * 
     * @param index The index to write
     */
    public void writeIndex(@NotNull Index index) {
        write(index);
    }

    /**
     * Deletes an Index object from disk.
     * 
     * @param uuid UUID of the index to delete
     */
    public void deleteIndex(@NotNull UUID uuid) {
        delete(uuid);
    }
}
