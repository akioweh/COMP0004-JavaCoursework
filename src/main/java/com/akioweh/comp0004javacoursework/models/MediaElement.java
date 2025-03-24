package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

import java.net.URI;


/**
 * Wraps a linked resource
 * to render an embed/preview of it.
 */
public class MediaElement extends LinkElement {
    @NotNull
    private MediaType mediaType;


    public MediaElement(@NotNull MediaType mediaType, @NotNull URI link) {
        super(link);
        this.mediaType = mediaType;
    }

    public MediaElement(@NotNull MediaType mediaType, @NotNull URI link, @NotNull String caption) {
        super(link, caption);
        this.mediaType = mediaType;
    }

    public @NotNull MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(@NotNull MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public static enum MediaType {
        IMAGE,
        VIDEO,
        AUDIO,
        OTHER
    }
}
