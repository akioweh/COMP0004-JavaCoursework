package com.akioweh.comp0004javacoursework.view;

import com.akioweh.comp0004javacoursework.engine.Engine;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for serving media files from the local media storage.
 */
@WebServlet(name = "Media Servlet", urlPatterns = "/media/*")
public class MediaServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MediaServlet.class.getName());

    @Inject
    private Engine engine;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Media file not specified");
            return;
        }

        // Remove leading slash
        String fileName = pathInfo.substring(1);

        // Sanitize the filename to prevent directory traversal attacks
        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");

        // Get the path to the media file
        Path mediaStoragePath = engine.getLocalMediaStoragePath();
        Path filePath = mediaStoragePath.resolve(fileName);

        // Check if the file exists
        if (!Files.exists(filePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Media file not found: " + fileName);
            return;
        }

        // Set the content type based on the file extension
        String contentType = getContentType(fileName);
        response.setContentType(contentType);

        // Set content length
        response.setContentLength((int) Files.size(filePath));

        // Stream the file to the response
        try (InputStream in = Files.newInputStream(filePath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error serving media file: " + fileName, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error serving media file");
        }
    }

    /**
     * Determines the content type based on the file extension.
     * 
     * @param fileName The name of the file
     * @return The content type
     */
    private String getContentType(String fileName) {
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = fileName.substring(dotIndex + 1).toLowerCase();
        }

        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            case "svg" -> "image/svg+xml";
            case "mp4" -> "video/mp4";
            case "webm" -> "video/webm";
            case "mp3" -> "audio/mpeg";
            case "wav" -> "audio/wav";
            case "ogg" -> "audio/ogg";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }
}
