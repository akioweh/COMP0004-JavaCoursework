package com.akioweh.comp0004javacoursework.api;

import com.akioweh.comp0004javacoursework.models.HTMLElement;
import com.akioweh.comp0004javacoursework.models.LinkElement;
import com.akioweh.comp0004javacoursework.models.MediaElement;
import com.akioweh.comp0004javacoursework.models.Note;
import com.akioweh.comp0004javacoursework.models.NoteElement;
import com.akioweh.comp0004javacoursework.models.TextElement;
import com.akioweh.comp0004javacoursework.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;


/**
 * API servlet for handling element operations.
 * Follows RESTful principles:
 * - GET: Retrieve an element
 * - POST: Create a new element
 * - PUT: Update an existing element
 * - DELETE: Delete an element
 * <p>
 * Path format: /api/element/{noteUuid}/{elementUuid}
 * For POST requests (creating new elements), the elementUuid is optional.
 * If provided, the new element will be inserted before the specified element.
 */
@WebServlet(name = "Element API", urlPatterns = "/api/element/*")
public class ElementApiServlet extends ApiServlet {

    /**
     * Parses the note UUID and element UUID from the path info.
     * The path info is expected to be in the format "/{noteUuid}/{elementUuid}".
     * For POST requests (creating new elements), the elementUuid is optional.
     *
     * @param request The HTTP request
     * @return An array containing the note UUID and element UUID (may be null)
     */
    protected UUID[] getUuidsFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }

        String[] parts = pathInfo.substring(1).split("/");
        if (parts.length < 1) {
            return null;
        }

        UUID noteUuid = Util.parseUUID(parts[0]);
        if (noteUuid == null) {
            return null;
        }

        UUID elementUuid = parts.length > 1 ? Util.parseUUID(parts[1]) : null;
        return new UUID[]{noteUuid, elementUuid};
    }

    /**
     * Gets the note and element from the path info.
     *
     * @param request        The HTTP request
     * @param response       The HTTP response
     * @param requireElement Whether the element is required
     * @return An array containing the note and element (may be null)
     * @throws IOException If an I/O error occurs
     */
    protected Object[] getNoteAndElement(HttpServletRequest request, HttpServletResponse response, boolean requireElement) throws IOException {
        UUID[] uuids = getUuidsFromPath(request);
        if (uuids == null) {
            sendBadRequest(response, "Invalid path format");
            return null;
        }

        UUID noteUuid = uuids[0];
        UUID elementUuid = uuids[1];

        Note note = engine.getNote(noteUuid);
        if (note == null) {
            sendNotFound(response, "Note not found");
            return null;
        }

        if (requireElement && elementUuid == null) {
            sendBadRequest(response, "Element UUID is required");
            return null;
        }

        NoteElement element = elementUuid != null ? note.getElement(elementUuid) : null;
        if (requireElement && element == null) {
            sendNotFound(response, "Element not found");
            return null;
        }

        return new Object[]{note, element};
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object[] noteAndElement = getNoteAndElement(request, response, false);
        if (noteAndElement == null) {
            return;
        }

        Note note = (Note) noteAndElement[0];
        NoteElement beforeElement = (NoteElement) noteAndElement[1];

        // Create a new element based on the element type
        String elementType = request.getParameter("elementType");
        if (elementType == null) {
            sendBadRequest(response, "Element type is required");
            return;
        }

        NoteElement newElement = createElement(elementType, request);
        if (newElement == null) {
            sendBadRequest(response, "Invalid element type");
            return;
        }

        // Insert the new element
        note.insertElement(beforeElement, newElement);
        engine.saveNote(note.getUuid());

        // Return success
        sendSuccess(response, "Element created successfully");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Debug logging to see what parameters are being received
        logger.info("[DEBUG_LOG] ElementApiServlet.doPut: Request parameters:");
        request.getParameterMap().forEach((key, value) ->
                logger.info("[DEBUG_LOG] " + key + " = " + String.join(", ", value))
        );

        Object[] noteAndElement = getNoteAndElement(request, response, true);
        if (noteAndElement == null) {
            return;
        }

        Note note = (Note) noteAndElement[0];
        NoteElement element = (NoteElement) noteAndElement[1];

        // Update the element based on its type
        switch (element) {
            case HTMLElement htmlElement -> updateHTMLElement(htmlElement, request);
            case TextElement textElement -> updateTextElement(textElement, request);
            case MediaElement mediaElement -> updateMediaElement(mediaElement, request);
            case LinkElement linkElement -> updateLinkElement(linkElement, request);
            case null, default -> {
                sendBadRequest(response, "Unsupported element type");
                return;
            }
        }

        // Save the note
        engine.saveNote(note.getUuid());

        // Return success
        sendSuccess(response, "Element updated successfully");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object[] noteAndElement = getNoteAndElement(request, response, true);
        if (noteAndElement == null) {
            return;
        }

        Note note = (Note) noteAndElement[0];
        NoteElement element = (NoteElement) noteAndElement[1];

        // Remove the element
        note.removeElement(element);
        engine.saveNote(note.getUuid());

        // Return success
        sendSuccess(response, "Element deleted successfully");
    }

    /**
     * Creates a new element based on the element type.
     *
     * @param elementType The type of element to create
     * @param request     The HTTP request containing element parameters
     * @return The new element, or null if the element type is invalid
     */
    private @Nullable NoteElement createElement(@NotNull String elementType, HttpServletRequest request) {
        return switch (elementType) {
            case "text" ->
                    new TextElement(request.getParameter("content") != null ? request.getParameter("content") : "");
            case "html" ->
                    new HTMLElement(request.getParameter("content") != null ? request.getParameter("content") : "");
            case "link" -> {
                String url = request.getParameter("url") != null ? request.getParameter("url") : "https://example.com";
                String displayText = request.getParameter("displayText") != null ? request.getParameter("displayText") : url;
                URI uri;
                try {
                    uri = URI.create(url);
                } catch (IllegalArgumentException e) {
                    // If the URL is invalid, log the error but don't throw an exception
                    logger.warning("Invalid URL: " + url);
                    // Use a default URL instead
                    uri = URI.create("https://example.com");
                }
                yield new LinkElement(uri, displayText);
            }
            case "media" -> {
                String url = request.getParameter("url") != null ? request.getParameter("url") : "https://example.com";
                String mediaTypeStr = request.getParameter("mediaType") != null ? request.getParameter("mediaType") : "IMAGE";
                URI uri;
                try {
                    uri = URI.create(url);
                } catch (IllegalArgumentException e) {
                    // If the URL is invalid, log the error but don't throw an exception
                    logger.warning("Invalid URL: " + url);
                    // Use a default URL instead
                    uri = URI.create("https://example.com");
                }
                MediaElement.MediaType mediaType;
                try {
                    mediaType = MediaElement.MediaType.valueOf(mediaTypeStr);
                } catch (IllegalArgumentException e) {
                    // If the media type is invalid, log the error but don't throw an exception
                    logger.warning("Invalid media type: " + mediaTypeStr);
                    // Use a default media type instead
                    mediaType = MediaElement.MediaType.IMAGE;
                }
                yield new MediaElement(mediaType, uri);
            }
            default -> null;
        };
    }

    /**
     * Updates a text element with the parameters from the request.
     *
     * @param element The text element to update
     * @param request The HTTP request containing element parameters
     */
    private void updateTextElement(@NotNull TextElement element, HttpServletRequest request) {
        String content = request.getParameter("content");
        if (content != null) {
            element.setContent(content);
        }

        String sectionTag = request.getParameter("sectionTag");
        if (sectionTag != null) {
            element.setSectionTag(sectionTag);
        }
    }

    /**
     * Updates a link element with the parameters from the request.
     *
     * @param element The link element to update
     * @param request The HTTP request containing element parameters
     */
    private void updateLinkElement(@NotNull LinkElement element, HttpServletRequest request) {
        String url = request.getParameter("url");
        if (url != null) {
            try {
                element.setUri(URI.create(url));
            } catch (IllegalArgumentException e) {
                // If the URL is invalid, log the error but don't throw an exception
                logger.warning("Invalid URL: " + url);
                // Use a default URL instead
                element.setUri(URI.create("https://example.com"));
            }
        }

        String displayText = request.getParameter("displayText");
        if (displayText != null) {
            element.setDisplayText(displayText);
        }

        String sectionTag = request.getParameter("sectionTag");
        if (sectionTag != null) {
            element.setSectionTag(sectionTag);
        }
    }

    /**
     * Updates an HTML element with the parameters from the request.
     *
     * @param element The HTML element to update
     * @param request The HTTP request containing element parameters
     */
    private void updateHTMLElement(@NotNull HTMLElement element, HttpServletRequest request) {
        String content = request.getParameter("content");
        if (content != null) {
            element.setContent(content);
        }

        String sectionTag = request.getParameter("sectionTag");
        if (sectionTag != null) {
            element.setSectionTag(sectionTag);
        }
    }

    /**
     * Updates a media element with the parameters from the request.
     *
     * @param element The media element to update
     * @param request The HTTP request containing element parameters
     */
    private void updateMediaElement(@NotNull MediaElement element, HttpServletRequest request) {
        String url = request.getParameter("url");
        if (url != null) {
            try {
                element.setUri(URI.create(url));
            } catch (IllegalArgumentException e) {
                // If the URL is invalid, log the error but don't throw an exception
                logger.warning("Invalid URL: " + url);
                // Use a default URL instead
                element.setUri(URI.create("https://example.com"));
            }
        }

        String mediaTypeStr = request.getParameter("mediaType");
        if (mediaTypeStr != null) {
            try {
                MediaElement.MediaType mediaType = MediaElement.MediaType.valueOf(mediaTypeStr);
                element.setMediaType(mediaType);
            } catch (IllegalArgumentException e) {
                // If the media type is invalid, log the error but don't throw an exception
                logger.warning("Invalid media type: " + mediaTypeStr);
                // Use a default media type instead
                element.setMediaType(MediaElement.MediaType.IMAGE);
            }
        }

        String displayText = request.getParameter("displayText");
        if (displayText != null) {
            element.setDisplayText(displayText);
        }

        String sectionTag = request.getParameter("sectionTag");
        if (sectionTag != null) {
            element.setSectionTag(sectionTag);
        }
    }
}
