package com.akioweh.comp0004javacoursework.api;

import com.akioweh.comp0004javacoursework.engine.Engine;
import com.akioweh.comp0004javacoursework.models.Note;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * API servlet for handling note operations.
 * Follows RESTful principles:
 * - GET: Retrieve a note
 * - POST: Create a new note
 * - PUT: Update an existing note
 * - DELETE: Delete a note
 */
@WebServlet(name = "Note API", urlPatterns = "/api/note/*")
public class NoteApiServlet extends ApiServlet {

    /**
     * Renders a note by forwarding to the note JSP.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param uuid The UUID of the note to render
     * @throws ServletException If a servlet error occurs
     * @throws IOException If an I/O error occurs
     */
    public static void renderNote(HttpServletRequest request, HttpServletResponse response, @NotNull UUID uuid) throws ServletException, IOException {
        var engine = Engine.getInstance();
        var note = engine.getNote(uuid);
        if (note == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Note not found");
            return;
        }
        request.setAttribute("note", note);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/render_note.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID uuid = getUuidFromPath(request);
        if (uuid == null) {
            sendBadRequest(response, "Note UUID is required");
            return;
        }

        Note note = engine.getNote(uuid);
        if (note == null) {
            sendNotFound(response, "Note not found");
            return;
        }

        // Forward to the note view
        request.setAttribute("note", note);
        request.setAttribute("editTargetUuid", null);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/render_note.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create a new note
        Note newNote = new Note();
        String title = request.getParameter("title");
        String brief = request.getParameter("brief");
        String tags = request.getParameter("tags");

        if (title != null && !title.isEmpty()) {
            newNote.setTitle(title);
        } else {
            newNote.setTitle("New Note");
        }

        if (brief != null && !brief.isEmpty()) {
            newNote.setBrief(brief);
        } else {
            newNote.setBrief("New Note on " + newNote.getCreated());
        }

        if (tags != null && !tags.isEmpty()) {
            var tagSet = new HashSet<>(Arrays.asList(tags.split(",")));
            tagSet.forEach(tag -> newNote.addTag(tag.trim()));
        }

        engine.addNote(newNote);

        // Redirect to the new note
        sendRedirect(request, response, "/note/" + newNote.getUuid());
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID uuid = getUuidFromPath(request);
        if (uuid == null) {
            sendBadRequest(response, "Note UUID is required");
            return;
        }

        Note note = engine.getNote(uuid);
        if (note == null) {
            sendNotFound(response, "Note not found");
            return;
        }

        // Update note properties
        String title = request.getParameter("title");
        String brief = request.getParameter("brief");
        String tags = request.getParameter("tags");

        if (title != null && !title.isEmpty()) {
            note.setTitle(title);
        }

        if (brief != null && !brief.isEmpty()) {
            note.setBrief(brief);
        }

        if (tags != null) {
            var tagSet = new HashSet<>(Arrays.asList(tags.split(",")));
            note.setTags(tagSet.stream().map(String::trim).collect(Collectors.toSet()));
        }

        engine.saveNote(note.getUuid());
        sendSuccess(response, "Note updated successfully");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID uuid = getUuidFromPath(request);
        if (uuid == null) {
            sendBadRequest(response, "Note UUID is required");
            return;
        }

        Note note = engine.getNote(uuid);
        if (note == null) {
            sendNotFound(response, "Note not found");
            return;
        }

        engine.deleteNote(uuid);
        sendSuccess(response, "Note deleted successfully");
    }
}
