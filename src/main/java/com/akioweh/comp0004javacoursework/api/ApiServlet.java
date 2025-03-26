package com.akioweh.comp0004javacoursework.api;

import com.akioweh.comp0004javacoursework.engine.Engine;
import com.akioweh.comp0004javacoursework.util.Util;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Base class for all API servlets.
 * Provides common functionality for handling requests and responses.
 */
public abstract class ApiServlet extends HttpServlet {
    protected static final Logger logger = Logger.getLogger(ApiServlet.class.getName());
    @Inject
    protected Engine engine;

    /**
     * Parses a UUID from the path info of the request.
     * The path info is expected to be in the format "/{uuid}".
     *
     * @param request The HTTP request
     * @return The parsed UUID, or null if the path info is invalid
     */
    protected @Nullable UUID getUuidFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }
        return Util.parseUUID(pathInfo.substring(1));
    }

    /**
     * Sends a bad request error response with the specified message.
     *
     * @param response The HTTP response
     * @param message The error message
     * @throws IOException If an I/O error occurs
     */
    protected void sendBadRequest(HttpServletResponse response, String message) throws IOException {
        logger.warning("Bad request: " + message);
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
    }

    /**
     * Sends a not found error response with the specified message.
     *
     * @param response The HTTP response
     * @param message The error message
     * @throws IOException If an I/O error occurs
     */
    protected void sendNotFound(HttpServletResponse response, String message) throws IOException {
        logger.warning("Not found: " + message);
        response.sendError(HttpServletResponse.SC_NOT_FOUND, message);
    }

    /**
     * Sends a success response with the specified message.
     *
     * @param response The HTTP response
     * @param message The success message
     * @throws IOException If an I/O error occurs
     */
    protected void sendSuccess(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(message);
    }

    /**
     * Sends a redirect response to the specified URL.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param path The path to redirect to
     * @throws IOException If an I/O error occurs
     */
    protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }
}
