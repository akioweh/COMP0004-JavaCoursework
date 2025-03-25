package com.akioweh.comp0004javacoursework.servlets;

import com.akioweh.comp0004javacoursework.engine.Engine;
import com.akioweh.comp0004javacoursework.models.HTMLElement;
import com.akioweh.comp0004javacoursework.models.LinkElement;
import com.akioweh.comp0004javacoursework.models.MediaElement;
import com.akioweh.comp0004javacoursework.models.TextElement;
import com.akioweh.comp0004javacoursework.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;


@WebServlet(name = "Add Element", urlPatterns = "/addElement")
public class AddElementServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("AddElementServlet.doPost");
        var noteUuid = Util.parseUUID(request.getParameter("noteUuid"));
        if (noteUuid == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid note UUID");
            return;
        }
        var beforeElementUuidString = request.getParameter("beforeElementUuid");
        var beforeElementUuid = Util.parseUUID(beforeElementUuidString);
        if (!Objects.equals(beforeElementUuidString, "null") && beforeElementUuid == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid before element UUID");
            return;
        }
        var engine = Engine.getInstance();
        var note = engine.getNote(noteUuid);
        if (note == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Note not found");
            return;
        }
        var beforeElement = beforeElementUuid == null ? null : note.getElement(beforeElementUuid);
        var elementType = request.getParameter("elementType");
        var newElement = switch (elementType) {
            case "text" -> new TextElement("");
            case "html" -> new HTMLElement("");
            case "link" -> new LinkElement(URI.create("example.com"), "");
            case "media" -> new MediaElement(MediaElement.MediaType.IMAGE, URI.create("example.com"));
            default -> throw new IllegalStateException("Unexpected value: " + elementType);
        };
        note.insertElement(beforeElement, newElement);

        // Save the note
        engine.saveNote(note.getUuid());

        // Return success
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
