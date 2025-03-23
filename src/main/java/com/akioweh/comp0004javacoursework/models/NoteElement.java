package com.akioweh.comp0004javacoursework.models;

import com.akioweh.comp0004javacoursework.engine.UUIO;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;


/**
 * Base form for all note elements.
 * <p>
 * Note elements are the building blocks of a note.
 * They can be text, links, images, etc.
 * </p>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LinkElement.class, name = "link"),
        @JsonSubTypes.Type(value = HTMLElement.class, name = "html"),
        @JsonSubTypes.Type(value = TextElement.class, name = "text"),
        @JsonSubTypes.Type(value = MediaElement.class, name = "media")
})
public abstract class NoteElement extends UUIO {
    /**
     * Used to populate HTML with section ids,
     * so we can reference specific sections in URLs.
     */
    protected @NotNull String sectionTag;

    public NoteElement() {
        this.sectionTag = "";
    }

    public NoteElement(@NotNull String sectionTag) {
        this.sectionTag = sectionTag;
    }

    public @NotNull String getSectionTag() {
        return sectionTag;
    }

    public void setSectionTag(@NotNull String sectionTag) {
        this.sectionTag = sectionTag;
    }
}
