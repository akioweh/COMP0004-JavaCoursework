package com.akioweh.comp0004javacoursework.models;

import com.akioweh.comp0004javacoursework.engine.Engine;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Vector;


/**
 * Class to aggregate a given collection of notes and other indices.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
public class Index extends UUIO {
    @NotNull
    private String name;
    @NotNull
    private String description;
    private final List<UUID> entries = new Vector<>();

    public Index(@NotNull String name, @NotNull String description) {
        this.name = name;
        this.description = description;
    }

    public Index() {
        this.name = "";
        this.description = "";
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    public @NotNull @UnmodifiableView List<UUID> getEntriesUuid() {
        return Collections.unmodifiableList(entries);
    }

    public @NotNull List<UUIO> getEntries() {
        return entries.stream().map(Engine.getInstance()::get).toList();
    }

    public void addEntry(@NotNull UUID item) {
        entries.add(item);
    }

    public void addEntry(@NotNull UUIO item) {
        entries.add(item.getUuid());
    }

    public void removeEntry(@NotNull UUID item) {
        entries.remove(item);
    }

    public void removeEntry(@NotNull UUIO item) {
        entries.remove(item.getUuid());
    }

    public List<Note> getNotes() {
        return getEntries().stream()
                .filter(o -> o instanceof Note)
                .map(o -> (Note) o)
                .toList();
    }

    public List<Index> getIndexes() {
        return getEntries().stream()
                .filter(o -> o instanceof Index)
                .map(o -> (Index) o)
                .toList();
    }

}
