package com.akioweh.comp0004javacoursework.engine;

import com.akioweh.comp0004javacoursework.models.Index;
import com.akioweh.comp0004javacoursework.models.Note;
import com.akioweh.comp0004javacoursework.models.UUIO;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


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
 * This is also a singleton class managed by CDI.
 * (Although it is only ever instantiated once by Engine anyway)
 * <p>
 * The entire class is package-private as it
 * should not be interfaced by anything other than Engine.
 */
@Singleton
class StorageHandler implements Serializable {
    private static final Logger logger = Logger.getLogger(StorageHandler.class.getName());
    protected Path localStoragePath;
    protected Path localMediaStoragePath;

    /**
     * Constructor for StorageHandler.
     *
     * @param localStoragePath      Path to store serialized objects
     * @param localMediaStoragePath Path to store media files
     */
    @Inject
    StorageHandler(@Named("localStoragePath") Path localStoragePath, @Named("localMediaStoragePath") Path localMediaStoragePath) {
        this.localStoragePath = localStoragePath;
        this.localMediaStoragePath = localMediaStoragePath;

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
    UUIO read(@NotNull UUID uuid) {
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
    void write(@NotNull UUIO data) {
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
    void delete(@NotNull UUID uuid) {
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
    boolean exists(@NotNull UUID uuid) {
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
    Note readNote(@NotNull UUID uuid) {
        return (Note) read(uuid);
    }

    /**
     * Writes a Note object to disk.
     *
     * @param note The note to write
     */
    void writeNote(@NotNull Note note) {
        write(note);
    }

    /**
     * Deletes a Note object from disk.
     *
     * @param uuid UUID of the note to delete
     */
    void deleteNote(@NotNull UUID uuid) {
        delete(uuid);
    }

    /**
     * Reads an Index object from disk.
     *
     * @param uuid UUID of the index to read
     * @return The index, or null if not found or error
     */
    Index readIndex(@NotNull UUID uuid) {
        return (Index) read(uuid);
    }

    /**
     * Writes an Index object to disk.
     *
     * @param index The index to write
     */
    void writeIndex(@NotNull Index index) {
        write(index);
    }

    /**
     * Deletes an Index object from disk.
     *
     * @param uuid UUID of the index to delete
     */
    void deleteIndex(@NotNull UUID uuid) {
        delete(uuid);
    }

    public Path getLocalStoragePath() {
        return localStoragePath;
    }

    /**
     * Gets the path to the local media storage directory.
     *
     * @return Path to the local media storage directory
     */
    public Path getLocalMediaStoragePath() {
        return localMediaStoragePath;
    }

    /**
     * Saves a file to the local media storage.
     *
     * @param fileName The name to save the file as
     * @param inputStream The input stream of the file to save
     * @return The name of the saved file
     * @throws IOException If an I/O error occurs
     */
    public String saveMediaFile(String fileName, InputStream inputStream) throws IOException {
        // Sanitize the filename to prevent directory traversal attacks
        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");

        // Ensure filename uniqueness by adding a timestamp if file exists
        Path filePath = localMediaStoragePath.resolve(fileName);
        if (Files.exists(filePath)) {
            String baseName = fileName;
            String extension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                baseName = fileName.substring(0, dotIndex);
                extension = fileName.substring(dotIndex);
            }
            fileName = baseName + "_" + System.currentTimeMillis() + extension;
            filePath = localMediaStoragePath.resolve(fileName);
        }

        // Save the file
        Files.copy(inputStream, filePath);
        logger.info("Saved media file: " + filePath);

        return fileName;
    }

    /**
     * Lists all files in the local media storage.
     *
     * @return List of filenames in the local media storage
     * @throws IOException If an I/O error occurs
     */
    public List<String> listMediaFiles() throws IOException {
        if (!Files.exists(localMediaStoragePath)) {
            return new ArrayList<>();
        }

        return Files.list(localMediaStoragePath)
                .filter(Files::isRegularFile)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
    }

    /**
     * Deletes a file from the local media storage.
     *
     * @param fileName The name of the file to delete
     * @return true if the file was deleted, false otherwise
     * @throws IOException If an I/O error occurs
     */
    public boolean deleteMediaFile(String fileName) throws IOException {
        // Sanitize the filename to prevent directory traversal attacks
        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");

        Path filePath = localMediaStoragePath.resolve(fileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            logger.info("Deleted media file: " + filePath);
            return true;
        } else {
            logger.warning("Tried to delete non-existent media file: " + filePath);
            return false;
        }
    }

    /**
     * Checks if a file exists in the local media storage.
     *
     * @param fileName The name of the file to check
     * @return true if the file exists, false otherwise
     */
    public boolean mediaFileExists(String fileName) {
        // Sanitize the filename to prevent directory traversal attacks
        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");

        Path filePath = localMediaStoragePath.resolve(fileName);
        return Files.exists(filePath);
    }
}
