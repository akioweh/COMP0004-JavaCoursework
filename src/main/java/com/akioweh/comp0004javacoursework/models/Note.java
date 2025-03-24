package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;


/**
 * Base form for all notes.
 * A composition of NoteElements.
 */
public class Note extends UUIO {
    @NotNull
    private final Date created = new Date();
    private final Set<String> tags = new HashSet<>();
    private final List<NoteElement> elements = new Vector<>();
    @NotNull
    private String title;
    @NotNull
    private String brief;
    @NotNull
    private Date modified = new Date();

    public Note(@NotNull String title, @NotNull String brief, @NotNull Vector<String> tags) {
        this.title = title;
        this.brief = brief;
        setTags(tags);
    }

    public Note() {
        this.title = "";
        this.brief = "";
    }

    public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
        modified = new Date();
    }

    public @NotNull String getBrief() {
        return brief;
    }

    public void setBrief(@NotNull String brief) {
        this.brief = brief;
        modified = new Date();
    }

    public @NotNull Date getCreated() {
        return created;
    }

    public @NotNull Date getModified() {
        return modified;
    }

    public @NotNull @UnmodifiableView List<NoteElement> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public @NotNull @UnmodifiableView Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    private void setTags(@NotNull Vector<String> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
        modified = new Date();
    }

    public boolean addTag(@NotNull String tag) {
        var res = tags.add(tag);
        if (res) {
            modified = new Date();
        }
        return res;
    }

    public boolean removeTag(@NotNull String tag) {
        var res = tags.remove(tag);
        if (res) {
            modified = new Date();
        }
        return res;
    }

    public void insertElement(@Nullable NoteElement before, @NotNull NoteElement element) {
        int index = before == null ? elements.size() : elements.indexOf(before);
        if (index == -1) {
            throw new IllegalArgumentException("Anchor element not found: " + before);
        }
        insertElement(index, element);
    }

    public void insertElement(int index, @NotNull NoteElement element) {
        elements.add(index, element);
        modified = new Date();
    }

    public void removeElement(@NotNull NoteElement element) {
        int index = elements.indexOf(element);
        if (index == -1) {
            throw new IllegalArgumentException("Element not found: " + element);
        }
        removeElement(index);
    }

    public void removeElement(int index) {
        elements.remove(index);
        modified = new Date();
    }

}
