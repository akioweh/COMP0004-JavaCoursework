package com.akioweh.comp0004javacoursework.servlets;

import com.akioweh.comp0004javacoursework.engine.Engine;
import com.akioweh.comp0004javacoursework.models.Index;
import com.akioweh.comp0004javacoursework.models.UUIO;
import com.akioweh.comp0004javacoursework.util.Util;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@WebServlet(name = "Index Renderer", urlPatterns = "/index/*")
public class IndexServlet extends HttpServlet {
    public static void renderIndex(HttpServletRequest request, HttpServletResponse response, @NotNull UUID uuid) throws ServletException, IOException {
        var index = Engine.getInstance().getIndex(uuid);
        if (index == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Index not found");
            return;
        }
        request.setAttribute("index", index);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/render_index.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var engine = Engine.getInstance();
        var pathInfo = request.getPathInfo();
        var uuidString = pathInfo != null ? pathInfo.substring(1) : "";  // Remove leading slash
        if (uuidString.isEmpty()) {
            // render the root index
            renderIndex(request, response, engine.getRootIndex().getUuid());
        } else {
            var uuid = Util.parseUUID(uuidString);
            if (uuid == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
                return;
            }
            if (uuid.equals(engine.getRootIndex().getUuid())) {
                // redirect to prefer the cleaner bare path for the root index
                response.sendRedirect(request.getContextPath() + "/index");
                return;
            }
            // render the index
            renderIndex(request, response, uuid);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var engine = Engine.getInstance();
        var pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Create a new index
            var newIndex = new Index();
            var name = request.getParameter("name");
            var description = request.getParameter("description");

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

            // Add the index to the engine
            engine.addIndex(newIndex);

            // Redirect to display the new index
            response.sendRedirect(request.getContextPath() + "/index/" + newIndex.getUuid());
        } else {
            // Update an existing index
            var indexUuid = Util.parseUUID(pathInfo.substring(1));
            if (indexUuid == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
                return;
            }

            var index = engine.getIndex(indexUuid);
            if (index == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Index not found");
                return;
            }

            // Check if we're updating the index itself or its entries
            var action = request.getParameter("action");
            if ("updateIndex".equals(action)) {
                // Update index properties
                var name = request.getParameter("name");
                var description = request.getParameter("description");

                if (name != null && !name.isEmpty()) {
                    index.setName(name);
                }

                if (description != null && !description.isEmpty()) {
                    index.setDescription(description);
                }

                engine.saveIndex(index.getUuid());
                response.sendRedirect(request.getContextPath() + "/index/" + index.getUuid());
            } else if ("addEntry".equals(action)) {
                // Add an entry to the index
                var entryUuid = Util.parseUUID(request.getParameter("entryUuid"));
                if (entryUuid == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid entry UUID");
                    return;
                }

                var entry = engine.get(entryUuid);
                if (entry == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Entry not found");
                    return;
                }

                index.addEntry(entry);
                engine.saveIndex(index.getUuid());
                response.sendRedirect(request.getContextPath() + "/index/" + index.getUuid());
            } else if ("removeEntry".equals(action)) {
                // Remove an entry from the index
                var entryUuid = Util.parseUUID(request.getParameter("entryUuid"));
                if (entryUuid == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid entry UUID");
                    return;
                }

                index.removeEntry(entryUuid);
                engine.saveIndex(index.getUuid());
                response.sendRedirect(request.getContextPath() + "/index/" + index.getUuid());
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        }
    }
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var engine = Engine.getInstance();
        var pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Index UUID is required");
            return;
        }

        var indexUuid = Util.parseUUID(pathInfo.substring(1));
        if (indexUuid == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
            return;
        }

        // Check if this is the root index
        if (indexUuid.equals(engine.getRootIndex().getUuid())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cannot delete the root index");
            return;
        }

        var index = engine.getIndex(indexUuid);
        if (index == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Index not found");
            return;
        }

        // Delete the index
        engine.deleteIndex(indexUuid);

        // Return success
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Index deleted successfully");
    }
}
