package com.akioweh.comp0004javacoursework.view;

import com.akioweh.comp0004javacoursework.models.Note;
import com.akioweh.comp0004javacoursework.models.NoteElement;

import java.util.UUID;


/**
 * ViewModel for the note view.
 * This record encapsulates the data needed by the note view, including the note itself and any view state.
 */
public record NoteViewState(Note note, UUID editTargetUuid) {
    /**
     * Creates a new noteViewState.
     *
     * @param note           The note to display
     * @param editTargetUuid The UUID of the element being edited, or null if no element is being edited
     */
    public NoteViewState {
    }

    /**
     * Gets the note.
     *
     * @return The note
     */
    @Override
    public Note note() {
        return note;
    }

    /**
     * Gets the UUID of the element being edited.
     *
     * @return The UUID of the element being edited, or null if no element is being edited
     */
    @Override
    public UUID editTargetUuid() {
        return editTargetUuid;
    }

    /**
     * Checks if the view is in edit mode.
     *
     * @return true if an element is being edited, false otherwise
     */
    public boolean editMode() {
        return editTargetUuid != null;
    }

    /**
     * Checks if the specified element is being edited.
     *
     * @param element The element to check
     * @return true if the element is being edited, false otherwise
     */
    public boolean isElementBeingEdited(NoteElement element) {
        return element != null && element.getUuid().equals(editTargetUuid);
    }
}
