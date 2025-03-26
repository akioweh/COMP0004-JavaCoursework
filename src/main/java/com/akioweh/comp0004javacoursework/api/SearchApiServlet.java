package com.akioweh.comp0004javacoursework.api;

import com.akioweh.comp0004javacoursework.models.Index;
import com.akioweh.comp0004javacoursework.models.Note;
import com.akioweh.comp0004javacoursework.util.NoteFilterSorter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * API servlet for handling search operations.
 * Follows RESTful principles:
 * - GET: Search for notes based on query parameters
 * <p>
 * This servlet provides a dedicated endpoint for searching notes.
 * It supports both tag-based and free-form content search, and allows sorting of results.
 * </p>
 */
@WebServlet(name = "Search API", urlPatterns = "/api/search/*")
public class SearchApiServlet extends ApiServlet {

    /**
     * Handles GET requests for searching notes.
     * Query parameters:
     * - searchTerm: Optional search term to filter notes by title, brief, or content
     * - tag: Optional tag to filter notes by
     * - sortOption: Optional sort option (TITLE_ASC, TITLE_DESC, CREATED_ASC, CREATED_DESC, MODIFIED_ASC, MODIFIED_DESC)
     * - limit: Optional maximum number of notes to return
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @throws ServletException If an error occurs while processing the request
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get search parameters from request
        String searchTerm = request.getParameter("searchTerm");
        String tag = request.getParameter("tag");
        String sortOptionStr = request.getParameter("sortOption");
        String limitStr = request.getParameter("limit");

        // Parse sort option
        NoteFilterSorter.SortOption sortOption = NoteFilterSorter.SortOption.MODIFIED_DESC; // Default sort option
        if (sortOptionStr != null && !sortOptionStr.isEmpty()) {
            try {
                sortOption = NoteFilterSorter.SortOption.valueOf(sortOptionStr);
            } catch (IllegalArgumentException e) {
                // Invalid sort option, use default
            }
        }

        // Parse limit
        int limit = 0; // No limit by default
        if (limitStr != null && !limitStr.isEmpty()) {
            try {
                limit = Integer.parseInt(limitStr);
            } catch (NumberFormatException e) {
                // Invalid limit, use default
            }
        }

        // Get all notes from the root index
        List<Note> allNotes = engine.getNotesIn(engine.getRootIndex());

        // Filter and sort notes
        List<Note> filteredNotes = NoteFilterSorter.filterAndSort(allNotes, searchTerm, tag, sortOption, limit);

        // Create a dynamic index to represent the search results
        Index searchResults = new Index();
        searchResults.setName("Search Results");
        String description = "Search results for ";
        if (searchTerm != null && !searchTerm.isEmpty()) {
            description += "term: \"" + searchTerm + "\" ";
        }
        if (tag != null && !tag.isEmpty()) {
            description += "tag: \"" + tag + "\" ";
        }
        searchResults.setDescription(description);
        searchResults.setDynamic(true);
        searchResults.setSearchTerm(searchTerm);
        searchResults.setTag(tag);
        searchResults.setSortOption(sortOption);

        // Add filtered notes to the search results index
        for (Note note : filteredNotes) {
            searchResults.addEntry(note);
        }

        // Forward to the search view servlet
        request.setAttribute("searchResults", searchResults);
        request.setAttribute("filteredNotes", filteredNotes);
        request.getRequestDispatcher("/search").forward(request, response);
    }
}