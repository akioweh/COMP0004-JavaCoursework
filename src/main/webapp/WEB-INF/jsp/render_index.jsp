<%--
  Author: akioweh
  23/03/2025 7:35 am
--%>
<%--@elvariable id="index" type="com.akioweh.comp0004javacoursework.models.Index"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.akioweh.comp0004javacoursework.engine.Engine" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Index View</title>
    <script>
        // Set the index UUID for index.js
        const indexUuid = '${index.uuid}';
    </script>
    <script src="${pageContext.request.contextPath}/static/js/index.js"></script>
</head>
<body>
<tags:header title="Index Details" showNewIndex="false" />
<div class="container">
    <div class="main">
        <div id="index-view">
            <div class="index-actions">
                <button onclick="editIndex()">Edit Index</button>
                <c:if test="${!index.uuid.equals(Engine.getInstance().rootIndex.uuid)}">
                <button onclick="deleteIndex()">Delete Index</button>
                </c:if>
                <button onclick="newIndex()">New Index</button>
            </div>
            <div class="index-details">
                <h2>${index.name}</h2>
                <p><strong>Description:</strong> ${index.description}</p>
            </div>
            <h3>Note Entries:</h3>
            <div class="entries-notes">
                <ol>
                    <c:forEach var="entry" items="${index.notes}">
                    <li>
                        <a href="note/${entry.uuid}">
                            ${entry.title}
                        </a>
                        <button class="remove-entry-btn" onclick="removeEntry('${entry.uuid}')" type="button">
                            Remove
                        </button>
                    </li>
                    </c:forEach>
                </ol>
            </div>
            <h3>Index Entries</h3>
            <div class="entries-indexes">
                <ol>
                    <c:forEach var="entry" items="${index.indexes}">
                        <c:if test="${!entry.equals(index)}">
                        <li>
                            <a href="index/${entry.uuid}">
                                ${entry.name}
                            </a>
                            <button class="remove-entry-btn" onclick="removeEntry('${entry.uuid}')" type="button">
                                Remove
                            </button>
                        </li>
                        </c:if>
                    </c:forEach>
                </ol>
            </div>
        </div>

        <div id="index-edit" style="display: none;">
            <h2>Edit Index</h2>
            <form id="edit-index-form" onsubmit="updateIndex(event)">
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" value="${index.name}" required>
                </div>
                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="description" rows="3" required>${index.description}</textarea>
                </div>
                <div class="form-actions">
                    <button type="submit">Save Changes</button>
                    <button type="button" onclick="cancelEdit()">Cancel</button>
                </div>
            </form>
        </div>

    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
