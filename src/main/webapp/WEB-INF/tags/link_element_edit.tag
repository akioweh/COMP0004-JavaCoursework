<%@ tag description="Renders a form for editing a link element" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="element" required="true" type="com.akioweh.comp0004javacoursework.models.LinkElement" %>
<%@ attribute name="noteUuid" required="true" type="java.util.UUID" %>

<form method="POST" action="${pageContext.request.contextPath}/api/element/${noteUuid}/${element.uuid}" class="element-edit-form">
    <div class="form-group">
        <label for="uri">URL:</label>
        <input type="url" id="uri" name="uri" value="${element.uri}" class="form-control" required>
    </div>
    <div class="form-group">
        <label for="displayText">Display Text:</label>
        <input type="text" id="displayText" name="displayText" value="${element.displayText}" class="form-control" required>
    </div>
    <div class="form-actions">
        <button type="submit" class="btn btn-primary">Save</button>
        <a href="${pageContext.request.contextPath}/note/${noteUuid}" class="btn btn-secondary">Cancel</a>
    </div>
</form>