<%--@elvariable id="mediaFiles" type="java.util.List<java.lang.String>"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Media Browser</title>
    <style>
        .media-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        
        .media-item {
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 10px;
            text-align: center;
        }
        
        .media-preview {
            width: 100%;
            height: 150px;
            object-fit: contain;
            margin-bottom: 10px;
        }
        
        .media-name {
            word-break: break-all;
            margin-bottom: 10px;
        }
        
        .media-url {
            font-size: 0.8em;
            color: #666;
            word-break: break-all;
            margin-bottom: 10px;
        }
        
        .copy-btn {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 5px 10px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 12px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 4px;
        }
        
        .upload-form {
            margin-top: 20px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        
        .upload-form h3 {
            margin-top: 0;
        }
        
        .upload-form input[type="file"] {
            margin: 10px 0;
        }
        
        .upload-form button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 15px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 4px;
        }
        
        .empty-message {
            margin-top: 20px;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 4px;
            text-align: center;
        }
    </style>
</head>
<body>
<tags:header title="Media Browser" showNewIndex="false" />
<div class="container">
    <div class="main">
        <h2>Media Browser</h2>
        <p>Upload and manage your media files here. You can use the links to these files in your media elements.</p>
        
        <div class="upload-form">
            <h3>Upload New Media File</h3>
            <form id="upload-form" enctype="multipart/form-data">
                <input type="file" id="file" name="file" required>
                <button type="submit">Upload</button>
            </form>
            <div id="upload-status"></div>
        </div>
        
        <c:choose>
            <c:when test="${empty mediaFiles}">
                <div class="empty-message">
                    <p>No media files have been uploaded yet. Use the form above to upload your first file.</p>
                </div>
            </c:when>
            <c:otherwise>
                <h3>Your Media Files</h3>
                <div class="media-grid">
                    <c:forEach var="fileName" items="${mediaFiles}">
                        <div class="media-item">
                            <c:set var="fileUrl" value="${pageContext.request.contextPath}/media/${fileName}" />
                            <c:set var="fileExt" value="${fn:substringAfter(fileName, '.')}" />
                            <c:set var="isImage" value="${fileExt eq 'jpg' or fileExt eq 'jpeg' or fileExt eq 'png' or fileExt eq 'gif' or fileExt eq 'webp' or fileExt eq 'svg'}" />
                            <c:set var="isVideo" value="${fileExt eq 'mp4' or fileExt eq 'webm'}" />
                            <c:set var="isAudio" value="${fileExt eq 'mp3' or fileExt eq 'wav' or fileExt eq 'ogg'}" />
                            
                            <c:choose>
                                <c:when test="${isImage}">
                                    <img src="${fileUrl}" alt="${fileName}" class="media-preview">
                                </c:when>
                                <c:when test="${isVideo}">
                                    <video src="${fileUrl}" class="media-preview" controls></video>
                                </c:when>
                                <c:when test="${isAudio}">
                                    <audio src="${fileUrl}" class="media-preview" controls></audio>
                                </c:when>
                                <c:otherwise>
                                    <div class="media-preview" style="display: flex; align-items: center; justify-content: center; background-color: #f8f9fa;">
                                        <span>${fileExt} file</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            
                            <div class="media-name">${fileName}</div>
                            <div class="media-url">${fileUrl}</div>
                            <button class="copy-btn" onclick="copyToClipboard('${fileUrl}')">Copy URL</button>
                            <button class="delete-btn" onclick="deleteFile('${fileName}')">Delete</button>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    // Function to copy URL to clipboard
    function copyToClipboard(text) {
        navigator.clipboard.writeText(text).then(function() {
            alert('URL copied to clipboard!');
        }, function(err) {
            console.error('Could not copy text: ', err);
        });
    }
    
    // Function to delete a file
    function deleteFile(fileName) {
        if (confirm('Are you sure you want to delete this file?')) {
            fetch('/api/media/' + fileName, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    window.location.reload();
                } else {
                    alert('Error deleting file');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error deleting file');
            });
        }
    }
    
    // Handle file upload
    document.getElementById('upload-form').addEventListener('submit', function(e) {
        e.preventDefault();
        
        const fileInput = document.getElementById('file');
        const file = fileInput.files[0];
        
        if (!file) {
            alert('Please select a file to upload');
            return;
        }
        
        const formData = new FormData();
        formData.append('file', file);
        
        const statusDiv = document.getElementById('upload-status');
        statusDiv.textContent = 'Uploading...';
        
        fetch('/api/media', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Upload failed');
            }
        })
        .then(data => {
            statusDiv.textContent = 'Upload successful!';
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            statusDiv.textContent = 'Upload failed: ' + error.message;
        });
    });
</script>

<jsp:include page="footer.jsp"/>
</body>
</html>