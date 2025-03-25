package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;


/**
 * Base form for all notes.
 * A composition of NoteElements.
 * Follows JavaBeans conventions for better integration with JSP and servlet ecosystem.
 */
public class Note extends UUIO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private Date created;
    private Set<String> tags;
    private List<NoteElement> elements;
    @NotNull
    private String title;
    @NotNull
    private String brief;
    @NotNull
    private Date modified;

    /**
     * Default constructor required for JavaBeans.
     */
    public Note() {
        super();
        this.created = new Date();
        this.modified = new Date();
        this.title = "";
        this.brief = "";
        this.tags = new HashSet<>();
        this.elements = new Vector<>();
    }

    /**
     * Constructor with title, brief, and tags.
     * 
     * @param title The title of the note
     * @param brief The brief description of the note
     * @param tags The tags for the note
     */
    public Note(@NotNull String title, @NotNull String brief, @NotNull Vector<String> tags) {
        super();
        this.created = new Date();
        this.modified = new Date();
        this.title = title;
        this.brief = brief;
        this.tags = new HashSet<>(tags);
        this.elements = new Vector<>();
    }

    /**
     * Gets the title of this note.
     * 
     * @return The title
     */
    public @NotNull String getTitle() {
        return title;
    }

    /**
     * Sets the title of this note.
     * 
     * @param title The new title
     */
    public void setTitle(@NotNull String title) {
        this.title = title;
        modified = new Date();
    }

    /**
     * Gets the brief description of this note.
     * 
     * @return The brief description
     */
    public @NotNull String getBrief() {
        return brief;
    }

    /**
     * Sets the brief description of this note.
     * 
     * @param brief The new brief description
     */
    public void setBrief(@NotNull String brief) {
        this.brief = brief;
        modified = new Date();
    }

    /**
     * Gets the creation date of this note.
     * 
     * @return The creation date
     */
    public @NotNull Date getCreated() {
        return created;
    }

    /**
     * Sets the creation date of this note.
     * This should be used with caution, as it changes the creation timestamp.
     * 
     * @param created The new creation date
     */
    public void setCreated(@NotNull Date created) {
        this.created = created;
    }

    /**
     * Gets the last modified date of this note.
     * 
     * @return The last modified date
     */
    public @NotNull Date getModified() {
        return modified;
    }

    /**
     * Sets the last modified date of this note.
     * This should be used with caution, as it changes the modification timestamp.
     * 
     * @param modified The new last modified date
     */
    public void setModified(@NotNull Date modified) {
        this.modified = modified;
    }

    /**
     * Gets an unmodifiable view of the elements in this note.
     * 
     * @return The elements
     */
    public @NotNull @UnmodifiableView List<NoteElement> getElements() {
        return Collections.unmodifiableList(elements);
    }

    /**
     * Sets the elements of this note.
     * 
     * @param elements The new elements
     */
    public void setElements(@NotNull List<NoteElement> elements) {
        this.elements = new ArrayList<>(elements);
        modified = new Date();
    }

    /**
     * Gets an unmodifiable view of the tags in this note.
     * 
     * @return The tags
     */
    public @NotNull @UnmodifiableView Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Sets the tags of this note.
     * 
     * @param tags The new tags
     */
    public void setTags(@NotNull Set<String> tags) {
        this.tags = new HashSet<>(tags);
        modified = new Date();
    }

    /**
     * Adds a tag to this note.
     * 
     * @param tag The tag to add
     * @return true if the tag was added, false if it was already present
     */
    public boolean addTag(@NotNull String tag) {
        var res = tags.add(tag);
        if (res) {
            modified = new Date();
        }
        return res;
    }

    /**
     * Removes a tag from this note.
     * 
     * @param tag The tag to remove
     * @return true if the tag was removed, false if it was not present
     */
    public boolean removeTag(@NotNull String tag) {
        var res = tags.remove(tag);
        if (res) {
            modified = new Date();
        }
        return res;
    }

    /**
     * Inserts an element into this note before another element.
     * 
     * @param before The element to insert before, or null to append
     * @param element The element to insert
     */
    public void insertElement(@Nullable NoteElement before, @NotNull NoteElement element) {
        int index = before == null ? elements.size() : elements.indexOf(before);
        if (index == -1) {
            throw new IllegalArgumentException("Anchor element not found: " + before);
        }
        insertElement(index, element);
    }

    /**
     * Inserts an element into this note at a specific index.
     * 
     * @param index The index to insert at
     * @param element The element to insert
     */
    public void insertElement(int index, @NotNull NoteElement element) {
        elements.add(index, element);
        modified = new Date();
    }

    /**
     * Removes an element from this note.
     * 
     * @param element The element to remove
     */
    public void removeElement(@NotNull NoteElement element) {
        int index = elements.indexOf(element);
        if (index == -1) {
            throw new IllegalArgumentException("Element not found: " + element);
        }
        removeElement(index);
    }

    /**
     * Removes an element from this note at a specific index.
     * 
     * @param index The index to remove at
     */
    public void removeElement(int index) {
        elements.remove(index);
        modified = new Date();
    }

    /**
     * Gets an element from this note by its UUID.
     * 
     * @param uuid The UUID of the element to get
     * @return The element, or null if not found
     */
    public @Nullable NoteElement getElement(@NotNull UUID uuid) {
        for (NoteElement element : elements) {
            if (element.getUuid().equals(uuid)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Gets the creation date of this note.
     * Alias for getCreated() for backward compatibility.
     * 
     * @return The creation date
     */
    public Date getCreatedDate() {
        return getCreated();
    }

    /**
     * Gets the last modified date of this note.
     * Alias for getModified() for backward compatibility.
     * 
     * @return The last modified date
     */
    public Date getModifiedDate() {
        return getModified();
    }
}
