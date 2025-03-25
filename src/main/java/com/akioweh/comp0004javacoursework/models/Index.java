package com.akioweh.comp0004javacoursework.models;

import com.akioweh.comp0004javacoursework.engine.Engine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Vector;


/**
 * Class to aggregate a given collection of notes and other indices.
 * Follows JavaBeans conventions for better integration with JSP and servlet ecosystem.
 */
public class Index extends UUIO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;
    @NotNull
    private String description;
    private List<UUID> entries;

    /**
     * Default constructor required for JavaBeans.
     */
    public Index() {
        super();
        this.name = "";
        this.description = "";
        this.entries = new Vector<>();
    }

    /**
     * Constructor with name and description.
     * 
     * @param name The name of the index
     * @param description The description of the index
     */
    public Index(@NotNull String name, @NotNull String description) {
        super();
        this.name = name;
        this.description = description;
        this.entries = new Vector<>();
    }

    /**
     * Gets the name of this index.
     * 
     * @return The name
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Sets the name of this index.
     * 
     * @param name The new name
     */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    /**
     * Gets the description of this index.
     * 
     * @return The description
     */
    public @NotNull String getDescription() {
        return description;
    }

    /**
     * Sets the description of this index.
     * 
     * @param description The new description
     */
    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    /**
     * Gets an unmodifiable view of the UUIDs of the entries in this index.
     * 
     * @return The UUIDs of the entries
     */
    public @NotNull @UnmodifiableView List<UUID> getEntriesUuid() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Sets the entries of this index by their UUIDs.
     * 
     * @param entries The new entries
     */
    public void setEntriesUuid(@NotNull List<UUID> entries) {
        this.entries = new ArrayList<>(entries);
    }

    /**
     * Gets the entries in this index as UUIO objects.
     * 
     * @return The entries
     */
    public @NotNull List<UUIO> getEntries() {
        return entries.stream().map(Engine.getInstance()::get).toList();
    }

    /**
     * Adds an entry to this index by its UUID.
     * 
     * @param item The UUID of the entry to add
     */
    public void addEntry(@NotNull UUID item) {
        entries.add(item);
    }

    /**
     * Adds an entry to this index.
     * 
     * @param item The entry to add
     */
    public void addEntry(@NotNull UUIO item) {
        entries.add(item.getUuid());
    }

    /**
     * Removes an entry from this index by its UUID.
     * 
     * @param item The UUID of the entry to remove
     */
    public void removeEntry(@NotNull UUID item) {
        entries.remove(item);
    }

    /**
     * Removes an entry from this index.
     * 
     * @param item The entry to remove
     */
    public void removeEntry(@NotNull UUIO item) {
        entries.remove(item.getUuid());
    }

    /**
     * Gets the notes in this index.
     * 
     * @return The notes
     */
    public List<Note> getNotes() {
        return getEntries().stream()
                .filter(o -> o instanceof Note)
                .map(o -> (Note) o)
                .toList();
    }

    /**
     * Gets the indexes in this index.
     * 
     * @return The indexes
     */
    public List<Index> getIndexes() {
        return getEntries().stream()
                .filter(o -> o instanceof Index)
                .map(o -> (Index) o)
                .toList();
    }
}
