package com.akioweh.comp0004javacoursework.view;

import com.akioweh.comp0004javacoursework.engine.Engine;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for displaying the media browser page.
 */
@WebServlet(name = "Media Browser", urlPatterns = "/media-browser")
public class MediaBrowserServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MediaBrowserServlet.class.getName());
    
    @Inject
    private Engine engine;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get the list of media files
            List<String> mediaFiles = engine.listMediaFiles();
            request.setAttribute("mediaFiles", mediaFiles);
            
            // Forward to the media browser JSP
            request.getRequestDispatcher("/WEB-INF/jsp/media_browser.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error displaying media browser", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error displaying media browser");
        }
    }
}