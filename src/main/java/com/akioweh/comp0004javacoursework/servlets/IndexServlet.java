package com.akioweh.comp0004javacoursework.servlets;

import com.akioweh.comp0004javacoursework.engine.Engine;
import com.akioweh.comp0004javacoursework.models.Index;
import com.akioweh.comp0004javacoursework.util.Util;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
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
        if (pathInfo != null) {
            // not allowed; post is for new indexes only
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid request. POST is for new indexes only; UUID not allowed.");
            return;
        }
        var newIndex = new Index();
        newIndex.setName("New Index");
        newIndex.setDescription("New Index on" + new Date());
        response.sendRedirect(request.getContextPath() + "/index/" + newIndex.getUuid());
    }
}
