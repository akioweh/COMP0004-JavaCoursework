package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.net.URI;


/**
 * Wraps a linked resource to render an embed/preview of it.
 * <p>
 * Follows JavaBeans conventions for better integration with JSP and servlet ecosystem.
 * </p>
 */
public class MediaElement extends LinkElement {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private MediaType mediaType;

    /**
     * Default constructor required for JavaBeans.
     */
    public MediaElement() {
        super();
        this.mediaType = MediaType.OTHER;
    }

    /**
     * Constructor with media type and link.
     * 
     * @param mediaType The media type
     * @param link The link to the media
     */
    public MediaElement(@NotNull MediaType mediaType, @NotNull URI link) {
        super(link);
        this.mediaType = mediaType;
    }

    /**
     * Constructor with media type, link, and caption.
     * 
     * @param mediaType The media type
     * @param link The link to the media
     * @param caption The caption for the media
     */
    public MediaElement(@NotNull MediaType mediaType, @NotNull URI link, @NotNull String caption) {
        super(link, caption);
        this.mediaType = mediaType;
    }

    /**
     * Gets the media type of this media element.
     * 
     * @return The media type
     */
    public @NotNull MediaType getMediaType() {
        return mediaType;
    }

    /**
     * Sets the media type of this media element.
     * 
     * @param mediaType The new media type
     */
    public void setMediaType(@NotNull MediaType mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * Enum for different types of media.
     */
    public static enum MediaType {
        IMAGE,
        VIDEO,
        AUDIO,
        OTHER
    }
}
