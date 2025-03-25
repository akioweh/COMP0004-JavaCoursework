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
        <label for="uri">URL:</label>
        <input type="url" id="uri" name="uri" value="${element.uri}" class="form-control" required>
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

<script>
    // Preview functionality
    document.addEventListener('DOMContentLoaded', function() {
        const mediaTypeSelect = document.getElementById('mediaType');
        const uriInput = document.getElementById('uri');
        const previewContainer = document.createElement('div');
        previewContainer.className = 'media-preview';
        previewContainer.innerHTML = '<h4>Preview</h4><div id="preview-content"></div>';

        const form = document.querySelector('.element-edit-form');
        form.appendChild(previewContainer);

        function updatePreview() {
            const mediaType = mediaTypeSelect.value;
            const uri = uriInput.value;
            const previewContent = document.getElementById('preview-content');

            if (!uri) {
                previewContent.innerHTML = '<p>Enter a URL to see a preview</p>';
                return;
            }

            switch(mediaType) {
                case 'IMAGE':
                    previewContent.innerHTML = `<img src="${uri}" alt="Preview" style="max-width: 100%;">`;
                    break;
                case 'VIDEO':
                    previewContent.innerHTML = `<video controls style="max-width: 100%;"><source src="${uri}" type="video/mp4">Preview not available</video>`;
                    break;
                case 'AUDIO':
                    previewContent.innerHTML = `<audio controls><source src="${uri}" type="audio/mpeg">Preview not available</audio>`;
                    break;
                default:
                    previewContent.innerHTML = `<a href="${uri}" target="_blank">Preview link</a>`;
            }
        }

        mediaTypeSelect.addEventListener('change', updatePreview);
        uriInput.addEventListener('input', updatePreview);

        // Initial preview
        updatePreview();
    });
</script>
