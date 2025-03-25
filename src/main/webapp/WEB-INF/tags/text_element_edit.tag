<%@ tag description="Renders a form for editing a text element" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="element" required="true" type="com.akioweh.comp0004javacoursework.models.TextElement" %>
<%@ attribute name="noteUuid" required="true" type="java.util.UUID" %>

<form method="POST" action="${pageContext.request.contextPath}/api/element/${noteUuid}/${element.uuid}" class="element-edit-form">
    <div class="form-group">
        <label for="content">Content:</label>
        <textarea id="content" name="content" rows="10" class="form-control">${element.content}</textarea>
    </div>
    <div class="form-actions">
        <button type="submit" class="btn btn-primary">Save</button>
        <a href="${pageContext.request.contextPath}/note/${noteUuid}" class="btn btn-secondary">Cancel</a>
    </div>
</form>