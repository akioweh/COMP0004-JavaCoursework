package com.akioweh.comp0004javacoursework.view;

import com.akioweh.comp0004javacoursework.models.Index;
import com.akioweh.comp0004javacoursework.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet for rendering indexes.
 * This servlet handles the view-related aspects of indexes,
 * while the IndexApiServlet handles the data operations.
 */
@WebServlet(name = "Index View", urlPatterns = "/index/*")
public class IndexViewServlet extends ViewServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        UUID uuid;
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // No index UUID provided, use the root index
            uuid = engine.getRootIndex().getUuid();
        } else {
            uuid = Util.parseUUID(pathInfo.substring(1));
            if (uuid == null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
                return;
            }
            
            if (uuid.equals(engine.getRootIndex().getUuid())) {
                // Redirect to the cleaner bare path for the root index
                sendRedirect(request, response, "/index");
                return;
            }
        }
        
        Index index = engine.getIndex(uuid);
        if (index == null) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, "Index not found");
            return;
        }
        
        // Set attributes for the JSP
        request.setAttribute("index", index);
        
        // Forward to the index JSP
        forwardToJsp(request, response, "/WEB-INF/jsp/render_index.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the API servlet
        request.getRequestDispatcher("/api/index" + (request.getPathInfo() != null ? request.getPathInfo() : "")).forward(request, response);
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the API servlet
        request.getRequestDispatcher("/api/index" + (request.getPathInfo() != null ? request.getPathInfo() : "")).forward(request, response);
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the API servlet
        request.getRequestDispatcher("/api/index" + (request.getPathInfo() != null ? request.getPathInfo() : "")).forward(request, response);
    }
}