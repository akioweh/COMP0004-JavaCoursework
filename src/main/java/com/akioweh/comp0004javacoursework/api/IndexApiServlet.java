package com.akioweh.comp0004javacoursework.api;

import com.akioweh.comp0004javacoursework.engine.Engine;
import com.akioweh.comp0004javacoursework.models.Index;
import com.akioweh.comp0004javacoursework.models.UUIO;
import com.akioweh.comp0004javacoursework.util.Util;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * API servlet for handling index operations.
 * Follows RESTful principles:
 * - GET: Retrieve an index
 * - POST: Create a new index
 * - PUT: Update an existing index
 * - DELETE: Delete an index
 * 
 * Path format: /api/index/{indexUuid}
 * For operations on entries, use the path format: /api/index/{indexUuid}/entry/{entryUuid}
 */
@WebServlet(name = "Index API", urlPatterns = {"/api/index/*", "/api/index/entry/*"})
public class IndexApiServlet extends ApiServlet {

    /**
     * Renders an index by forwarding to the index JSP.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param uuid The UUID of the index to render
     * @throws ServletException If a servlet error occurs
     * @throws IOException If an I/O error occurs
     */
    public static void renderIndex(HttpServletRequest request, HttpServletResponse response, @NotNull UUID uuid) throws ServletException, IOException {
        var engine = Engine.getInstance();
        var index = engine.getIndex(uuid);
        if (index == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Index not found");
            return;
        }
        request.setAttribute("index", index);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/render_index.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Parses the index UUID and entry UUID from the path info.
     * The path info is expected to be in the format "/{indexUuid}" or "/{indexUuid}/entry/{entryUuid}".
     * 
     * @param request The HTTP request
     * @return An array containing the index UUID and entry UUID (may be null)
     */
    protected UUID[] getUuidsFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return new UUID[] { null, null };
        }

        String[] parts = pathInfo.substring(1).split("/");
        if (parts.length < 1) {
            return new UUID[] { null, null };
        }

        UUID indexUuid = Util.parseUUID(parts[0]);

        // Check if this is an entry operation
        if (parts.length >= 3 && "entry".equals(parts[1])) {
            UUID entryUuid = Util.parseUUID(parts[2]);
            return new UUID[] { indexUuid, entryUuid };
        }

        return new UUID[] { indexUuid, null };
    }

    /**
     * Gets the index and entry from the path info.
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @param requireEntry Whether the entry is required
     * @return An array containing the index and entry (may be null)
     * @throws IOException If an I/O error occurs
     */
    protected Object[] getIndexAndEntry(HttpServletRequest request, HttpServletResponse response, boolean requireEntry) throws IOException {
        UUID[] uuids = getUuidsFromPath(request);
        UUID indexUuid = uuids[0];
        UUID entryUuid = uuids[1];

        if (indexUuid == null) {
            return new Object[] { null, null };
        }

        Index index = engine.getIndex(indexUuid);
        if (index == null) {
            sendNotFound(response, "Index not found");
            return null;
        }

        if (requireEntry && entryUuid == null) {
            sendBadRequest(response, "Entry UUID is required");
            return null;
        }

        UUIO entry = entryUuid != null ? engine.get(entryUuid) : null;
        if (requireEntry && entry == null) {
            sendNotFound(response, "Entry not found");
            return null;
        }

        return new Object[] { index, entry };
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID[] uuids = getUuidsFromPath(request);
        UUID indexUuid = uuids[0];

        if (indexUuid == null) {
            // No index UUID provided, render the root index
            renderIndex(request, response, engine.getRootIndex().getUuid());
            return;
        }

        Index index = engine.getIndex(indexUuid);
        if (index == null) {
            sendNotFound(response, "Index not found");
            return;
        }

        // Forward to the index view
        request.setAttribute("index", index);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/render_index.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID[] uuids = getUuidsFromPath(request);
        UUID indexUuid = uuids[0];
        UUID entryUuid = uuids[1];

        if (indexUuid == null) {
            // Create a new index
            Index newIndex = new Index();
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            if (name != null && !name.isEmpty()) {
                newIndex.setName(name);
            } else {
                newIndex.setName("New Index");
            }

            if (description != null && !description.isEmpty()) {
                newIndex.setDescription(description);
            } else {
                newIndex.setDescription("New Index created on " + new Date());
            }

            engine.addIndex(newIndex);

            // Redirect to the new index
            sendRedirect(request, response, "/index/" + newIndex.getUuid());
            return;
        }

        // If we have an index UUID but no entry UUID, it's an error
        if (entryUuid == null) {
            sendBadRequest(response, "Entry UUID is required for adding entries");
            return;
        }

        // Add an entry to the index
        Object[] indexAndEntry = getIndexAndEntry(request, response, true);
        if (indexAndEntry == null) {
            return;
        }

        Index index = (Index) indexAndEntry[0];
        UUIO entry = (UUIO) indexAndEntry[1];

        index.addEntry(entry);
        engine.saveIndex(index.getUuid());

        // Return success
        sendSuccess(response, "Entry added successfully");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object[] indexAndEntry = getIndexAndEntry(request, response, false);
        if (indexAndEntry == null) {
            return;
        }

        Index index = (Index) indexAndEntry[0];

        // Update index properties
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        if (name != null && !name.isEmpty()) {
            index.setName(name);
        }

        if (description != null && !description.isEmpty()) {
            index.setDescription(description);
        }

        engine.saveIndex(index.getUuid());

        // Return success
        sendSuccess(response, "Index updated successfully");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID[] uuids = getUuidsFromPath(request);
        UUID indexUuid = uuids[0];
        UUID entryUuid = uuids[1];

        if (indexUuid == null) {
            sendBadRequest(response, "Index UUID is required");
            return;
        }

        Index index = engine.getIndex(indexUuid);
        if (index == null) {
            sendNotFound(response, "Index not found");
            return;
        }

        // Check if this is the root index
        if (indexUuid.equals(engine.getRootIndex().getUuid())) {
            sendBadRequest(response, "Cannot delete the root index");
            return;
        }

        if (entryUuid != null) {
            // Remove an entry from the index
            UUIO entry = engine.get(entryUuid);
            if (entry == null) {
                sendNotFound(response, "Entry not found");
                return;
            }

            index.removeEntry(entry);
            engine.saveIndex(index.getUuid());

            // Return success
            sendSuccess(response, "Entry removed successfully");
            return;
        }

        // Delete the index
        engine.deleteIndex(indexUuid);

        // Return success
        sendSuccess(response, "Index deleted successfully");
    }
}
