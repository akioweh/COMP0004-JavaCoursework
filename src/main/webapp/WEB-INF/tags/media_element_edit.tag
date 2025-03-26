<%@ tag description="Renders a form for editing a media element" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="element" required="true" type="com.akioweh.comp0004javacoursework.models.MediaElement" %>
<%@ attribute name="noteUuid" required="true" type="java.util.UUID" %>

<form onsubmit="updateElement(event, '${noteUuid}', '${element.uuid}')" class="element-edit-form">
    <div class="form-group">
        <label for="mediaType">Media Type:</label>
        <select id="mediaType" name="mediaType" class="form-control" required>
            <option value="IMAGE" ${element.mediaType == 'IMAGE' ? 'selected' : ''}>Image</option>
            <option value="VIDEO" ${element.mediaType == 'VIDEO' ? 'selected' : ''}>Video</option>
            <option value="AUDIO" ${element.mediaType == 'AUDIO' ? 'selected' : ''}>Audio</option>
        </select>
    </div>
    <div class="form-group">
        <label for="url">URL:</label>
        <input type="text" id="url" name="url" value="${element.uri}" class="form-control" required>
        <small class="form-text text-muted">
            Need to use your own media files? <a href="${pageContext.request.contextPath}/media-browser" target="_blank">Upload files</a> first, then copy the link here.
            You can use relative URLs (e.g., /media/image.jpg) or absolute URLs (e.g., https://example.com/image.jpg).
        </small>
    </div>
    <div class="form-group">
        <label for="displayText">Caption/Alt Text:</label>
        <input type="text" id="displayText" name="displayText" value="${element.displayText}" class="form-control">
    </div>
    <div class="form-actions">
        <button type="submit" class="btn btn-primary">Save</button>
        <a href="${pageContext.request.contextPath}/note/${noteUuid}" class="btn btn-secondary">Cancel</a>
    </div>
</form>

<script src="${pageContext.request.contextPath}/static/js/media_preview.js"></script>
