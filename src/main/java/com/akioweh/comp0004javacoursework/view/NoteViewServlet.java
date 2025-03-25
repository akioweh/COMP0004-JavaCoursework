package com.akioweh.comp0004javacoursework.view;

import com.akioweh.comp0004javacoursework.models.Note;
import com.akioweh.comp0004javacoursework.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet for rendering notes.
 * This servlet handles the view-related aspects of notes,
 * while the NoteApiServlet handles the data operations.
 */
@WebServlet(name = "Note View", urlPatterns = "/note/*")
public class NoteViewServlet extends ViewServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // No note UUID provided, redirect to the home page
            sendRedirect(request, response, "/");
            return;
        }

        UUID uuid = Util.parseUUID(pathInfo.substring(1));
        if (uuid == null) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
            return;
        }

        if (uuid.equals(engine.getRootIndex().getUuid())) {
            // Redirect to the index page if this is the root index
            sendRedirect(request, response, "/index");
            return;
        }

        Note note = engine.getNote(uuid);
        if (note == null) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, "Note not found");
            return;
        }

        // Set attributes for the JSP
        request.setAttribute("note", note);
        request.setAttribute("editTargetUuid", Util.parseUUID(request.getParameter("edit")));

        // Forward to the note JSP
        forwardToJsp(request, response, "/WEB-INF/jsp/render_note.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the API servlet
        request.getRequestDispatcher("/api/note" + (request.getPathInfo() != null ? request.getPathInfo() : "")).forward(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the API servlet
        request.getRequestDispatcher("/api/note" + (request.getPathInfo() != null ? request.getPathInfo() : "")).forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the API servlet
        request.getRequestDispatcher("/api/note" + (request.getPathInfo() != null ? request.getPathInfo() : "")).forward(request, response);
    }
}