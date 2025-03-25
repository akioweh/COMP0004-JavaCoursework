package com.akioweh.comp0004javacoursework.view;

import com.akioweh.comp0004javacoursework.engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Base class for all view servlets.
 * Provides common functionality for handling requests and responses.
 */
public abstract class ViewServlet extends HttpServlet {
    protected static final Logger logger = Logger.getLogger(ViewServlet.class.getName());
    protected final Engine engine = Engine.getInstance();

    /**
     * Forwards the request to a JSP page.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param jspPath The path to the JSP page
     * @throws ServletException If a servlet error occurs
     * @throws IOException If an I/O error occurs
     */
    protected void forwardToJsp(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull String jspPath) throws ServletException, IOException {
        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    /**
     * Sends a redirect response to the specified URL.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param path The path to redirect to
     * @throws IOException If an I/O error occurs
     */
    protected void sendRedirect(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull String path) throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    /**
     * Sends an error response with the specified status code and message.
     *
     * @param response The HTTP response
     * @param statusCode The HTTP status code
     * @param message The error message
     * @throws IOException If an I/O error occurs
     */
    protected void sendError(@NotNull HttpServletResponse response, int statusCode, @NotNull String message) throws IOException {
        logger.warning("Error " + statusCode + ": " + message);
        response.sendError(statusCode, message);
    }
}