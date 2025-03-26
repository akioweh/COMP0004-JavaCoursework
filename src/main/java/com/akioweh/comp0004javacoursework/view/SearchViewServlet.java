package com.akioweh.comp0004javacoursework.view;

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
 * Servlet for rendering search results.
 * This servlet handles the view-related aspects of search,
 * while the SearchApiServlet handles the data operations.
 */
@WebServlet(name = "Search View", urlPatterns = "/search/*")
public class SearchViewServlet extends ViewServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get search parameters from request
        String searchTerm = request.getParameter("searchTerm");
        String tag = request.getParameter("tag");
        String sortOptionStr = request.getParameter("sortOption");
        String limitStr = request.getParameter("limit");

        // If we have search parameters, process them
        if ((searchTerm != null && !searchTerm.isEmpty()) || (tag != null && !tag.isEmpty())) {
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

            // Set attributes for the JSP
            request.setAttribute("index", searchResults);
            request.setAttribute("filteredNotes", filteredNotes);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("tag", tag);
            request.setAttribute("sortOption", sortOption);
            request.setAttribute("dynamicSearch", true);

            // Forward to the search results JSP
            forwardToJsp(request, response, "/WEB-INF/jsp/search_results.jsp");
        } else {
            // If no search parameters, show the search form
            forwardToJsp(request, response, "/WEB-INF/jsp/search.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the API servlet
        request.getRequestDispatcher("/api/search" + (request.getPathInfo() != null ? request.getPathInfo() : "")).forward(request, response);
    }
}
