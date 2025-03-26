package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;


/**
 * Base form for all note elements.
 * <p>
 * Note elements are the building blocks of a note.
 * They can be text, links, images, etc.
 * </p>
 * <p>
 * Follows JavaBeans conventions for better integration with JSP and servlet ecosystem.
 * </p>
 */
public abstract class NoteElement extends UUIO {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Used to populate HTML with section ids,
     * so we can reference specific sections in URLs.
     */
    protected @NotNull String sectionTag;

    /**
     * Default constructor required for JavaBeans.
     */
    public NoteElement() {
        super();
        this.sectionTag = "";
    }

    /**
     * Constructor with a section tag.
     * 
     * @param sectionTag The section tag
     */
    public NoteElement(@NotNull String sectionTag) {
        super();
        this.sectionTag = sectionTag;
    }

    /**
     * Gets the section tag of this element.
     * 
     * @return The section tag
     */
    public @NotNull String getSectionTag() {
        return sectionTag;
    }

    /**
     * Sets the section tag of this element.
     * 
     * @param sectionTag The new section tag
     */
    public void setSectionTag(@NotNull String sectionTag) {
        this.sectionTag = sectionTag;
    }

    /**
     * Extracts searchable text from this element.
     * This method is used for text search functionality.
     * 
     * @return The searchable text from this element
     */
    public abstract @NotNull String extractSearchableText();
}
