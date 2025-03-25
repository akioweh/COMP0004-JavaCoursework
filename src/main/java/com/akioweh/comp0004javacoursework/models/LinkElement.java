package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.net.URI;


/**
 * Represents a linked (nonlocal) resource.
 * Can be an internal link to another note,
 * or an external URL (to a page, media, etc.).
 * <p>
 * Note: we're using URI objects instead of URLs
 * because URLs are too restrictive.
 * (e.g., they don't allow relative paths)
 * </p>
 * <p>
 * Follows JavaBeans conventions for better integration with JSP and servlet ecosystem.
 * </p>
 */
public class LinkElement extends NoteElement {
    @Serial
    private static final long serialVersionUID = 1L;

    private @NotNull URI uri;
    private @NotNull String displayText;

    /**
     * Default constructor required for JavaBeans.
     */
    public LinkElement() {
        super();
        try {
            this.uri = new URI("");
            this.displayText = "";
        } catch (Exception e) {
            // This should never happen with an empty URI
            throw new RuntimeException("Failed to create empty URI", e);
        }
    }

    /**
     * Constructor with URI.
     * 
     * @param uri The URI
     */
    public LinkElement(@NotNull URI uri) {
        super();
        this.uri = uri;
        this.displayText = uri.toString();
    }

    /**
     * Constructor with URI and display text.
     * 
     * @param uri The URI
     * @param displayText The display text
     */
    public LinkElement(@NotNull URI uri, @NotNull String displayText) {
        super();
        this.uri = uri;
        this.displayText = displayText;
    }

    /**
     * Gets the URI of this link element.
     * 
     * @return The URI
     */
    public @NotNull URI getUri() {
        return uri;
    }

    /**
     * Sets the URI of this link element.
     * 
     * @param uri The new URI
     */
    public void setUri(@NotNull URI uri) {
        this.uri = uri;
    }

    /**
     * Gets the display text of this link element.
     * 
     * @return The display text
     */
    public @NotNull String getDisplayText() {
        return displayText;
    }

    /**
     * Sets the display text of this link element.
     * 
     * @param displayText The new display text
     */
    public void setDisplayText(@NotNull String displayText) {
        this.displayText = displayText;
    }
}
