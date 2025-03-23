package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

/**
 * Wraps a linked resource
 * to render an embed/preview of it.
 */
public class MediaElement extends LinkElement {
    public MediaElement(@NotNull URI link) {
        super(link);
    }

    public MediaElement(@NotNull URI link, @NotNull String caption) {
        super(link, caption);
    }
}
