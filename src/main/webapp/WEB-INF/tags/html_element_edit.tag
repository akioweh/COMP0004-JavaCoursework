<%@ tag description="Renders a form for editing an HTML element" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="element" required="true" type="com.akioweh.comp0004javacoursework.models.HTMLElement" %>
<%@ attribute name="noteUuid" required="true" type="java.util.UUID" %>

<form method="POST" action="${pageContext.request.contextPath}/api/element/${noteUuid}/${element.uuid}" class="element-edit-form">
    <div class="form-group">
        <label for="content">HTML Content:</label>
        <textarea id="content" name="content" rows="10" class="form-control html-editor">${element.content}</textarea>
    </div>
    <div class="form-actions">
        <button type="submit" class="btn btn-primary">Save</button>
        <a href="${pageContext.request.contextPath}/note/${noteUuid}" class="btn btn-secondary">Cancel</a>
    </div>
</form>

<script>
    // Initialize a simple HTML editor if available
    document.addEventListener('DOMContentLoaded', function() {
        const textarea = document.querySelector('.html-editor');
        if (textarea && typeof CodeMirror !== 'undefined') {
            CodeMirror.fromTextArea(textarea, {
                mode: 'htmlmixed',
                lineNumbers: true,
                theme: 'default'
            });
        }
    });
</script>