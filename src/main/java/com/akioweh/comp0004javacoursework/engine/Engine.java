package com.akioweh.comp0004javacoursework.engine;

import com.akioweh.comp0004javacoursework.models.Index;
import com.akioweh.comp0004javacoursework.models.Note;
import com.akioweh.comp0004javacoursework.models.UUIO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


/**
 * "Frontman" class analogous to a database engine.
 * <p>
 * The engine is responsible for returning the
 * object for a given UUID.
 * To do this, it interacts with the StorageHandler
 * to load new data from the disk while caching (storing references to)
 * previously loaded objects to avoid excessive disk I/O.
 * </p>
 * <p>
 * The engine also instructs the StorageHandler to save
 * or delete disk data according to memory-state changes.
 * </p>
 * <p>
 * <strong>Engine is a singleton class managed by CDI.</strong>
 * </p>
 */
@Named
@Singleton
public class Engine implements Serializable {
    private static final Logger logger = Logger.getLogger(Engine.class.getName());
    private static boolean initialized = false;
    private final StorageHandler storageHandler;
    private final HashMap<UUID, Note> noteCache = new HashMap<>();
    private final HashMap<UUID, Index> indexCache = new HashMap<>();
    /**
     * The root index is the top-level index.
     * It always exists, cannot be deleted or manually modified,
     * and keeps track of all existing indexes and notes.
     */
    @NotNull
    private final UUID rootIndexUuid;

    @Inject
    Engine(StorageHandler storageHandler) {
        if (initialized) {  // just in case CDI is misconfigured
            throw new IllegalStateException("Engine is already initialized");
        }
        this.storageHandler = storageHandler;
        var localStoragePath = storageHandler.getLocalStoragePath();
        this.rootIndexUuid = initRootIndex(localStoragePath);
        logger.info("Engine initialized with storage path: " + localStoragePath);
        initialized = true;
    }

    private @NotNull UUID initRootIndex(@NotNull Path storagePath) {
        var rootIndexPath = storagePath.resolve("rootIndex.id");
        if (!rootIndexPath.toFile().exists()) {
            logger.info("Did not find root index pointer; assuming empty storage. Initializing...");
            try {
                var rootIndex = new Index("Root", "Collection of all existing notes and indexes");
                var rootIndexUuid = rootIndex.getUuid();
                Files.writeString(rootIndexPath, rootIndexUuid.toString());
                indexCache.put(rootIndexUuid, rootIndex);
                storageHandler.writeIndex(rootIndex);
                return rootIndexUuid;
            } catch (Exception e) {
                throw new RuntimeException("Failed to create root index file: " + rootIndexPath, e);
            }
        } else {
            try {
                var rootIndexUuid = UUID.fromString(Files.readString(rootIndexPath));
                logger.info("Found root index pointer: " + rootIndexUuid);
                return rootIndexUuid;
            } catch (Exception e) {
                throw new RuntimeException("Failed to determine root index UUID from " + rootIndexPath, e);
            }
        }
    }

    /**
     * Regular use should prefer the more specifically typed
     * getNote() or getIndex() methods.
     */
    public UUIO get(@NotNull UUID uuid) {
        UUIO object = noteCache.get(uuid);
        if (object != null) {
            return object;
        }
        object = indexCache.get(uuid);
        if (object != null) {
            return object;
        }
        object = storageHandler.read(uuid);
        if (object != null) {
            if (object instanceof Note note) {
                noteCache.put(note.getUuid(), note);
            } else if (object instanceof Index index) {
                indexCache.put(index.getUuid(), index);
            }
        }
        return object;
    }

    private boolean exists(@NotNull UUID uuid) {
        return noteCache.containsKey(uuid) || indexCache.containsKey(uuid) || storageHandler.exists(uuid);
    }

    public Index getRootIndex() {
        return getIndex(rootIndexUuid);
    }

    public void saveRootIndex() {
        saveIndex(rootIndexUuid);
    }

    public Note getNote(@NotNull UUID uuid) {
        var note = noteCache.get(uuid);
        if (note != null) {
            return note;
        }
        note = storageHandler.readNote(uuid);
        if (note != null) {
            noteCache.put(uuid, note);
        }
        return note;
    }

    public void addNote(@NotNull Note note) {
        if (exists(note.getUuid())) {
            throw new IllegalStateException("Note already exists");
        }
        noteCache.put(note.getUuid(), note);
        storageHandler.writeNote(note);
        getRootIndex().addEntry(note);
        saveRootIndex();
    }

    public void saveNote(@NotNull UUID noteUuid) {
        var note = noteCache.get(noteUuid);
        if (note == null) {
            throw new IllegalStateException("Note not found in cache");
        }
        storageHandler.writeNote(note);
    }

    public void deleteNote(@NotNull UUID noteUuid) {
        if (!exists(noteUuid)) {
            throw new IllegalStateException("Note does not exist");
        }
        noteCache.remove(noteUuid);
        storageHandler.deleteNote(noteUuid);
        getRootIndex().removeEntry(noteUuid);
        saveRootIndex();
    }

    public Index getIndex(@NotNull UUID uuid) {
        var index = indexCache.get(uuid);
        if (index != null) {
            return index;
        }
        index = storageHandler.readIndex(uuid);
        if (index != null) {
            indexCache.put(uuid, index);
        }
        return index;
    }

    public void addIndex(@NotNull Index index) {
        if (exists(index.getUuid())) {
            throw new IllegalStateException("Index already exists");
        }
        indexCache.put(index.getUuid(), index);
        storageHandler.writeIndex(index);
        getRootIndex().addEntry(index);
        saveRootIndex();
    }

    public List<UUIO> getEntriesIn(@NotNull Index index) {
        return index.getEntriesUuid().stream()
                .map(this::get)
                .toList();
    }

    public List<Note> getNotesIn(@NotNull Index index) {
        return getEntriesIn(index).stream()
                .filter(Note.class::isInstance)
                .map(Note.class::cast)
                .toList();
    }

    public List<Index> getIndexesIn(@NotNull Index index) {
        return getEntriesIn(index).stream()
                .filter(Index.class::isInstance)
                .map(Index.class::cast)
                .toList();
    }

    public void saveIndex(@NotNull UUID indexUuid) {
        var index = indexCache.get(indexUuid);
        if (index == null) {
            throw new IllegalStateException("Index not found in cache");
        }
        storageHandler.writeIndex(index);
    }

    public void deleteIndex(@NotNull UUID indexUuid) {
        if (!exists(indexUuid)) {
            throw new IllegalStateException("Index does not exist");
        }
        if (indexUuid.equals(rootIndexUuid)) {
            throw new IllegalStateException("Cannot delete root index");
        }
        indexCache.remove(indexUuid);
        storageHandler.deleteIndex(indexUuid);
        getRootIndex().removeEntry(indexUuid);
        saveRootIndex();
    }

    public void saveAll() {
        noteCache.values().forEach(storageHandler::writeNote);
        indexCache.values().forEach(storageHandler::writeIndex);
    }
}
