document.addEventListener('DOMContentLoaded', function() {
    const mediaTypeSelect = document.getElementById('mediaType');
    const urlInput = document.getElementById('url');
    const previewContainer = document.createElement('div');
    previewContainer.className = 'media-preview';
    previewContainer.innerHTML = '<h4>Live Preview</h4><div id="preview-content"></div>';

    const form = document.querySelector('.element-edit-form');
    form.appendChild(previewContainer);

    function updatePreview() {
        const mediaType = mediaTypeSelect.value;
        const uri = urlInput.value;
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

    // Use both input and keyup events to ensure maximum compatibility
    mediaTypeSelect.addEventListener('change', updatePreview);
    urlInput.addEventListener('input', updatePreview);
    urlInput.addEventListener('keyup', updatePreview);
    urlInput.addEventListener('paste', function() {
        // Use setTimeout to ensure the paste operation completes
        setTimeout(updatePreview, 10);
    });

    // Initial preview
    updatePreview();
});
