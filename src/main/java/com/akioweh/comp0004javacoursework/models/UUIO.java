package com.akioweh.comp0004javacoursework.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;


/**
 * Universally Uniquely Identifiable Object / Universal Unrestricted IO.
 * (master of funny)
 * Self-explanatory.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Index.class, name = "index"),
        @JsonSubTypes.Type(value = Note.class, name = "note"),
})
public abstract class UUIO {
    @JsonProperty("uuid")
    private final UUID uuid = UUID.randomUUID();

    public @NotNull UUID getUuid() {
        return uuid;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UUIO)) return false;
        return Objects.equals(uuid, ((UUIO) obj).uuid);
    }
}
