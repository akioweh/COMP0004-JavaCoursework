package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;


/**
 * Represents a note element that is just plain text.
 * <p>
 * Its contents can be rendered using variable methods,
 * depending on the subclass or other properties.
 * </p>
 * <p>
 * Follows JavaBeans conventions for better integration with JSP and servlet ecosystem.
 * </p>
 */
public class TextElement extends NoteElement {
    @Serial
    private static final long serialVersionUID = 1L;

    private @NotNull String content;

    /**
     * Default constructor required for JavaBeans.
     */
    public TextElement() {
        super();
        this.content = "";
    }

    /**
     * Constructor with content.
     * 
     * @param content The text content
     */
    public TextElement(@NotNull String content) {
        super();
        this.content = content;
    }

    /**
     * Gets the content of this text element.
     * 
     * @return The content
     */
    public @NotNull String getContent() {
        return content;
    }

    /**
     * Sets the content of this text element.
     * 
     * @param content The new content
     */
    public void setContent(@NotNull String content) {
        this.content = content;
    }

    /**
     * Extracts searchable text from this element.
     * For text elements, this is simply the content.
     * 
     * @return The content of this text element
     */
    @Override
    public @NotNull String extractSearchableText() {
        return getContent();
    }
}
