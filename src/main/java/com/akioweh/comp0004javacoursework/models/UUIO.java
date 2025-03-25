package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * Universally Uniquely Identifiable Object / Universal Unrestricted IO.
 * Base class for all model objects, providing UUID-based identification.
 * Implements Serializable to support standard Java serialization.
 */
public abstract class UUIO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID uuid;

    /**
     * Default constructor that generates a new UUID.
     */
    public UUIO() {
        this.uuid = UUID.randomUUID();
    }

    /**
     * Constructor with a specific UUID.
     * 
     * @param uuid The UUID to use
     */
    public UUIO(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets the UUID of this object.
     * 
     * @return The UUID
     */
    public @NotNull UUID getUuid() {
        return uuid;
    }

    /**
     * Sets the UUID of this object.
     * This should be used with caution, as changing the UUID can break references.
     * 
     * @param uuid The new UUID
     */
    public void setUuid(@NotNull UUID uuid) {
        this.uuid = uuid;
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
