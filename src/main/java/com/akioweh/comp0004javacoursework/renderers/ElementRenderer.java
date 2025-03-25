package com.akioweh.comp0004javacoursework.renderers;

import com.akioweh.comp0004javacoursework.models.NoteElement;
import org.jetbrains.annotations.NotNull;


/**
 * This exists purely for organizational purposes.
 */
public interface ElementRenderer<T extends NoteElement> {
    /**
     * Renders the HTML for non-editable display
     */
    @NotNull String render(@NotNull T element);

    /**
     * Augmented form of render which also allows the user to edit its fields
     */
    @NotNull String renderEdit(@NotNull T element);
}
