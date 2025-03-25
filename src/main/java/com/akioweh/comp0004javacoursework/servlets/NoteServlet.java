package com.akioweh.comp0004javacoursework.servlets;

import com.akioweh.comp0004javacoursework.engine.Engine;
import com.akioweh.comp0004javacoursework.models.LinkElement;
import com.akioweh.comp0004javacoursework.models.MediaElement;
import com.akioweh.comp0004javacoursework.models.Note;
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
            newNote.setTitle("New Note");
            newNote.setBrief("New Note on " + newNote.getCreated());
            engine.addNote(newNote);

            // redirect to display the new note
            response.sendRedirect(request.getContextPath() + "/note/" + newNote.getUuid());
        } else {
            // edit existing note
            var noteUuid = Util.parseUUID(pathInfo.substring(1));
            var elementUuid = Util.parseUUID(request.getParameter("edit"));
            if (elementUuid == null || noteUuid == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
                return;
            }
            var note = engine.getNote(noteUuid);
            if (note == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Note not found");
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
            }
            // save the note
            engine.saveNote(note.getUuid());
        }
    }

}
