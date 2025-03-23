package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;


/**
 * Represents a note element that is just plain text.
 * <p>
 * Its contents can be rendered using variable methods,
 * depending on the subclass or other properties.
 * </p>
 */
public class TextElement extends NoteElement {
    private @NotNull String content;

    public TextElement(@NotNull String content) {
        this.content = content;
    }

    public @NotNull String getContent() {
        return content;
    }

    public void setContent(@NotNull String content) {
        this.content = content;
    }
}
