package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

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
 */
public class LinkElement extends NoteElement {
    private @NotNull URI uri;
    private @NotNull String displayText;

    public LinkElement(@NotNull URI uri) {
        this.uri = uri;
        this.displayText = uri.toString();
    }

    public LinkElement(@NotNull URI uri, @NotNull String displayText) {
        this.uri = uri;
        this.displayText = displayText;
    }

    public @NotNull URI getUri() {
        return uri;
    }

    public void setUri(@NotNull URI uri) {
        this.uri = uri;
    }

    public @NotNull String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(@NotNull String displayText) {
        this.displayText = displayText;
    }
}
