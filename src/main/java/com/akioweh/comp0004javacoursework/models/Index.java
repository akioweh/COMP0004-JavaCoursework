package com.akioweh.comp0004javacoursework.models;

import com.akioweh.comp0004javacoursework.util.NoteFilterSorter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;
    @NotNull
    private String description;
    private List<UUID> entries;

    // Dynamic index properties
    private boolean isDynamic;
    @Nullable
    private String searchTerm;
    @Nullable
    private String tag;
    @Nullable
    private NoteFilterSorter.SortOption sortOption;

    /**
     * Default constructor required for JavaBeans.
     */
    public Index() {
        super();
        this.name = "";
        this.description = "";
        this.entries = new Vector<>();
        this.isDynamic = false;
        this.searchTerm = null;
        this.tag = null;
        this.sortOption = null;
    }

    /**
     * Constructor with name and description.
     *
     * @param name        The name of the index
     * @param description The description of the index
     */
    public Index(@NotNull String name, @NotNull String description) {
        super();
        this.name = name;
        this.description = description;
        this.entries = new Vector<>();
        this.isDynamic = false;
        this.searchTerm = null;
        this.tag = null;
        this.sortOption = null;
    }

    /**
     * Constructor for dynamic index.
     *
     * @param name        The name of the index
     * @param description The description of the index
     * @param searchTerm  The search term to filter notes by
     * @param tag         The tag to filter notes by
     * @param sortOption  The sort option to use
     */
    public Index(@NotNull String name, @NotNull String description, 
                 @Nullable String searchTerm, @Nullable String tag, 
                 @Nullable NoteFilterSorter.SortOption sortOption) {
        super();
        this.name = name;
        this.description = description;
        this.entries = new Vector<>();
        this.isDynamic = true;
        this.searchTerm = searchTerm;
        this.tag = tag;
        this.sortOption = sortOption;
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
     * Checks if this index is dynamic.
     *
     * @return true if this index is dynamic, false otherwise
     */
    public boolean isDynamic() {
        return isDynamic;
    }

    /**
     * Sets whether this index is dynamic.
     *
     * @param dynamic true if this index is dynamic, false otherwise
     */
    public void setDynamic(boolean dynamic) {
        isDynamic = dynamic;
    }

    /**
     * Gets the search term of this dynamic index.
     *
     * @return The search term, or null if not set
     */
    public @Nullable String getSearchTerm() {
        return searchTerm;
    }

    /**
     * Sets the search term of this dynamic index.
     *
     * @param searchTerm The new search term
     */
    public void setSearchTerm(@Nullable String searchTerm) {
        this.searchTerm = searchTerm;
    }

    /**
     * Gets the tag of this dynamic index.
     *
     * @return The tag, or null if not set
     */
    public @Nullable String getTag() {
        return tag;
    }

    /**
     * Sets the tag of this dynamic index.
     *
     * @param tag The new tag
     */
    public void setTag(@Nullable String tag) {
        this.tag = tag;
    }

    /**
     * Gets the sort option of this dynamic index.
     *
     * @return The sort option, or null if not set
     */
    public @Nullable NoteFilterSorter.SortOption getSortOption() {
        return sortOption;
    }

    /**
     * Sets the sort option of this dynamic index.
     *
     * @param sortOption The new sort option
     */
    public void setSortOption(@Nullable NoteFilterSorter.SortOption sortOption) {
        this.sortOption = sortOption;
    }
}
