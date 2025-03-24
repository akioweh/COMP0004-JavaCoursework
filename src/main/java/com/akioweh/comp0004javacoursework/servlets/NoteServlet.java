package com.akioweh.comp0004javacoursework.servlets;

import com.akioweh.comp0004javacoursework.engine.Engine;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;


@WebServlet(name = "Note Renderer", urlPatterns = "/note/*")
public class NoteServlet extends HttpServlet {
    public void renderNote(HttpServletRequest request, HttpServletResponse response, @NotNull UUID uuid) throws ServletException, IOException {
        var note = Engine.getInstance().getNote(uuid);
        if (note == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Note not found");
            return;
        }
        request.setAttribute("obj", note);
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/render_note.jsp");
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
        var uuid = UUID.fromString(uuidString);
        if (uuid.equals(engine.getRootIndex().getUuid())) {
            // redirect
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }
        // render the note
        renderNote(request, response, uuid);
    }

}
