package com.akioweh.comp0004javacoursework.servlets;

import com.akioweh.comp0004javacoursework.engine.Engine;
import com.akioweh.comp0004javacoursework.models.HTMLElement;
import com.akioweh.comp0004javacoursework.models.LinkElement;
import com.akioweh.comp0004javacoursework.models.MediaElement;
import com.akioweh.comp0004javacoursework.models.Note;
import com.akioweh.comp0004javacoursework.models.NoteElement;
import com.akioweh.comp0004javacoursework.models.TextElement;
import com.akioweh.comp0004javacoursework.util.Util;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;


@WebServlet(name = "Note Renderer", urlPatterns = "/note/*")
public class NoteServlet extends HttpServlet {
    public void renderNote(HttpServletRequest request, HttpServletResponse response, @NotNull UUID uuid) throws ServletException, IOException {
        var note = Engine.getInstance().getNote(uuid);
        if (note == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Note not found");
            return;
        }
        request.setAttribute("note", note);
        request.setAttribute("editTargetUuid", Util.parseUUID(request.getParameter("edit")));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/render_note.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var engine = Engine.getInstance();
        var pathInfo = request.getPathInfo();
        var uuidString = pathInfo != null ? pathInfo.substring(1) : "";
        if (uuidString.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
            return;
        }
        var uuid = Util.parseUUID(uuidString);
        if (uuid == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
            return;
        }
        if (uuid.equals(engine.getRootIndex().getUuid())) {
            // redirect
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }
        // render the note
        renderNote(request, response, uuid);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var engine = Engine.getInstance();
        var pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            // new note
            var newNote = new Note();
            var title = request.getParameter("title");
            var brief = request.getParameter("brief");
            var tags = request.getParameter("tags");

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

            // redirect to display the new note
            response.sendRedirect(request.getContextPath() + "/note/" + newNote.getUuid());
        } else {
            // edit existing note
            var noteUuid = Util.parseUUID(pathInfo.substring(1));
            if (noteUuid == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
                return;
            }

            var note = engine.getNote(noteUuid);
            if (note == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Note not found");
                return;
            }

            // Check if we're updating the note itself or an element
            var action = request.getParameter("action");
            if ("updateNote".equals(action)) {
                // Update note properties
                var title = request.getParameter("title");
                var brief = request.getParameter("brief");
                var tags = request.getParameter("tags");

                if (title != null && !title.isEmpty()) {
                    note.setTitle(title);
                }

                if (brief != null && !brief.isEmpty()) {
                    note.setBrief(brief);
                }

                if (tags != null) {
                    var tagSet = new HashSet<>(Arrays.asList(tags.split(",")));
                    note.setTags(tagSet.stream().map(String::trim).collect(java.util.stream.Collectors.toSet()));
                }

                engine.saveNote(note.getUuid());
                response.sendRedirect(request.getContextPath() + "/note/" + note.getUuid());
                return;
            } else if ("deleteElement".equals(action)) {
                // Delete an element
                var elementUuid = Util.parseUUID(request.getParameter("elementUuid"));
                if (elementUuid == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid element UUID");
                    return;
                }

                var element = note.getElement(elementUuid);
                if (element == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Element not found");
                    return;
                }

                note.removeElement(element);
                engine.saveNote(note.getUuid());
                response.sendRedirect(request.getContextPath() + "/note/" + note.getUuid());
                return;
            }

            // If no specific action, assume we're editing an element
            var elementUuid = Util.parseUUID(request.getParameter("edit"));
            if (elementUuid == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid element UUID");
                return;
            }

            // find the element
            var element = note.getElement(elementUuid);
            if (element == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Element not found");
                return;
            }
            // update the element
            switch (element) {
                case HTMLElement htmlElement: {
                    var content = request.getParameter("content");
                    if (content != null) {
                        htmlElement.setContent(content);
                    }
                    break;
                }
                case TextElement textElement: {
                    var content = request.getParameter("content");
                    if (content != null) {
                        textElement.setContent(content);
                    }
                    break;
                }
                case MediaElement mediaElement: {
                    var mediaTypeString = request.getParameter("mediaType");
                    if (mediaTypeString != null) {
                        var mediaType = MediaElement.MediaType.valueOf(mediaTypeString);
                        mediaElement.setMediaType(mediaType);
                    }
                    var uri = request.getParameter("uri");
                    var displayText = request.getParameter("displayText");
                    if (uri != null) {
                        mediaElement.setUri(URI.create(uri));
                    }
                    if (displayText != null) {
                        mediaElement.setDisplayText(displayText);
                    }
                    break;
                }
                case LinkElement linkElement: {
                    var uri = request.getParameter("uri");
                    var displayText = request.getParameter("displayText");
                    if (uri != null) {
                        linkElement.setUri(URI.create(uri));
                    }
                    if (displayText != null) {
                        linkElement.setDisplayText(displayText);
                    }
                    break;
                }
                default: {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported element type: " + element.getClass().getSimpleName());
                    return;
                }
            }
            // save the note
            engine.saveNote(note.getUuid());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var engine = Engine.getInstance();
        var pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Note UUID is required");
            return;
        }

        var noteUuid = Util.parseUUID(pathInfo.substring(1));
        if (noteUuid == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
            return;
        }

        var note = engine.getNote(noteUuid);
        if (note == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Note not found");
            return;
        }

        // Delete the note
        engine.deleteNote(noteUuid);

        // Return success
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Note deleted successfully");
    }
}
