package com.akioweh.comp0004javacoursework.util;

import com.akioweh.comp0004javacoursework.models.Note;
import com.akioweh.comp0004javacoursework.models.NoteElement;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for filtering and sorting notes.
 */
public class NoteFilterSorter {

    /**
     * Sort options for notes.
     */
    public enum SortOption {
        TITLE_ASC,
        TITLE_DESC,
        CREATED_ASC,
        CREATED_DESC,
        MODIFIED_ASC,
        MODIFIED_DESC
    }

    /**
     * Filter and sort a list of notes.
     *
     * @param notes       The list of notes to filter and sort
     * @param searchTerm  Optional search term to filter notes by title, brief, or content (null or empty for no filtering)
     * @param tag         Optional tag to filter notes by (null or empty for no filtering)
     * @param sortOption  Sort option to apply
     * @param limit       Maximum number of notes to return (0 or negative for no limit)
     * @return A filtered and sorted list of notes
     */
    public static List<Note> filterAndSort(
            @NotNull List<Note> notes,
            String searchTerm,
            String tag,
            SortOption sortOption,
            int limit) {

        // Create a stream of notes
        var stream = notes.stream();

        // Apply search term filter if provided
        if (searchTerm != null && !searchTerm.isEmpty()) {
            String lowerSearchTerm = searchTerm.toLowerCase();
            stream = stream.filter(note ->
                    note.getTitle().toLowerCase().contains(lowerSearchTerm) ||
                    note.getBrief().toLowerCase().contains(lowerSearchTerm) ||
                    searchInNoteElements(note, lowerSearchTerm)
            );
        }

        // Apply tag filter if provided
        if (tag != null && !tag.isEmpty()) {
            stream = stream.filter(note -> note.getTags().contains(tag));
        }

        // Apply sorting
        stream = stream.sorted(getComparator(sortOption));

        // Apply limit if provided
        if (limit > 0) {
            stream = stream.limit(limit);
        }

        // Collect and return the result
        return stream.collect(Collectors.toList());
    }

    /**
     * Search within the elements of a note for a search term.
     *
     * @param note The note to search in
     * @param searchTerm The search term to look for (should be lowercase)
     * @return true if the search term is found in any element, false otherwise
     */
    private static boolean searchInNoteElements(@NotNull Note note, @NotNull String searchTerm) {
        for (NoteElement element : note.getElements()) {
            if (element.extractSearchableText().toLowerCase().contains(searchTerm)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a comparator for the specified sort option.
     *
     * @param sortOption The sort option
     * @return A comparator for the specified sort option
     */
    private static Comparator<Note> getComparator(SortOption sortOption) {
        return switch (sortOption) {
            case TITLE_ASC -> Comparator.comparing(Note::getTitle);
            case TITLE_DESC -> Comparator.comparing(Note::getTitle).reversed();
            case CREATED_ASC -> Comparator.comparing(Note::getCreated);
            case CREATED_DESC -> Comparator.comparing(Note::getCreated).reversed();
            case MODIFIED_ASC -> Comparator.comparing(Note::getModified);
            case MODIFIED_DESC -> Comparator.comparing(Note::getModified).reversed();
        };
    }

    /**
     * Get recent notes from a list of notes.
     *
     * @param notes The list of notes
     * @param limit Maximum number of notes to return
     * @return A list of recent notes, sorted by modification date (newest first)
     */
    public static List<Note> getRecentNotes(@NotNull List<Note> notes, int limit) {
        return filterAndSort(notes, null, null, SortOption.MODIFIED_DESC, limit);
    }

    /**
     * Filter notes by a search term.
     *
     * @param notes      The list of notes to filter
     * @param searchTerm The search term to filter by
     * @return A filtered list of notes
     */
    public static List<Note> searchNotes(@NotNull List<Note> notes, String searchTerm) {
        return filterAndSort(notes, searchTerm, null, SortOption.TITLE_ASC, 0);
    }

    /**
     * Filter notes by a tag.
     *
     * @param notes The list of notes to filter
     * @param tag   The tag to filter by
     * @return A filtered list of notes
     */
    public static List<Note> filterByTag(@NotNull List<Note> notes, String tag) {
        return filterAndSort(notes, null, tag, SortOption.TITLE_ASC, 0);
    }
}
