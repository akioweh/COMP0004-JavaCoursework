package com.akioweh.comp0004javacoursework.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;


/**
 * API servlet for handling media file operations.
 * Follows RESTful principles:
 * - GET: List media files or get a specific media file
 * - POST: Upload a new media file
 * - DELETE: Delete a media file
 */
@WebServlet(name = "Media API", urlPatterns = "/api/media/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 50)    // 50 MB
public class MediaApiServlet extends ApiServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = getFileNameFromPath(request);

        if (fileName == null || fileName.isEmpty()) {
            // List all media files
            List<String> mediaFiles = engine.listMediaFiles();
            sendJsonResponse(response, mediaFiles);
        } else {
            // Check if the file exists
            if (engine.mediaFileExists(fileName)) {
                // Return the URL for the file
                String fileUrl = engine.getMediaFileUrl(fileName);
                sendJsonResponse(response, fileUrl);
            } else {
                sendNotFound(response, "Media file not found");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle file upload
        Part filePart = request.getPart("file");
        if (filePart == null) {
            sendBadRequest(response, "No file uploaded");
            return;
        }

        String fileName = getSubmittedFileName(filePart);
        if (fileName == null || fileName.isEmpty()) {
            sendBadRequest(response, "Invalid file name");
            return;
        }

        // Save the file
        String savedFileName = engine.saveMediaFile(fileName, filePart.getInputStream());
        String fileUrl = engine.getMediaFileUrl(savedFileName);

        // Return the URL for the uploaded file
        sendJsonResponse(response, fileUrl);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = getFileNameFromPath(request);

        if (fileName == null || fileName.isEmpty()) {
            sendBadRequest(response, "File name is required");
            return;
        }

        // Delete the file
        boolean deleted = engine.deleteMediaFile(fileName);
        if (deleted) {
            sendSuccess(response, "Media file deleted successfully");
        } else {
            sendNotFound(response, "Media file not found");
        }
    }

    /**
     * Extracts the file name from the request path.
     *
     * @param request The HTTP request
     * @return The file name, or null if not found
     */
    private String getFileNameFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }

        // Remove leading slash
        return pathInfo.substring(1);
    }

    /**
     * Gets the submitted file name from a Part.
     *
     * @param part The Part containing the file
     * @return The file name
     */
    private String getSubmittedFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) {
            return null;
        }

        for (String token : contentDisposition.split(";")) {
            token = token.trim();
            if (token.startsWith("filename=")) {
                return token.substring(9).replace("\"", "");
            }
        }

        return null;
    }

    /**
     * Sends a JSON response with the specified data.
     *
     * @param response The HTTP response
     * @param data     The data to send (String or List<String>)
     * @throws IOException If an I/O error occurs
     */
    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (data instanceof String) {
            // For a single string, return a JSON object with a "url" field
            String json = "{\"url\":\"" + data + "\"}";
            response.getWriter().write(json);
        } else if (data instanceof List) {
            // For a list of strings, return a JSON array
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) data;
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    json.append(",");
                }
                json.append("\"").append(list.get(i)).append("\"");
            }
            json.append("]");
            response.getWriter().write(json.toString());
        } else {
            // Fallback for other types
            response.getWriter().write("{}");
        }
    }
}
