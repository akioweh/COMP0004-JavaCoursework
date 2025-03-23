package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

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
