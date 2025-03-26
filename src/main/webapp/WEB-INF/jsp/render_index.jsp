<%--@elvariable id="index" type="com.akioweh.comp0004javacoursework.models.Index"--%>
<%--@elvariable id="engine" type="com.akioweh.comp0004javacoursework.engine.Engine"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
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
                <c:if test="${!index.uuid.equals(engine.rootIndex.uuid)}">
                <button onclick="editIndex()">Edit Index</button>
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
                    <c:forEach var="entry" items="${engine.getNotesIn(index)}">
                    <li>
                        <a href="/note/${entry.uuid}" class="entry-content">
                            <div class="entry-title">${entry.title}</div>
                            <div class="entry-brief">${entry.brief}</div>
                        </a>
                        <c:if test="${!index.uuid.equals(engine.rootIndex.uuid)}">
                        <button class="remove-entry-btn" onclick="removeEntry('${entry.uuid}')" type="button">
                            Remove
                        </button>
                        </c:if>
                    </li>
                    </c:forEach>
                </ol>
                <c:if test="${!index.uuid.equals(engine.rootIndex.uuid)}">
                <div class="add-entry">
                    <h4>Add Note</h4>
                    <select id="note-select">
                        <option value="">Select a note to add</option>
                        <c:forEach var="note" items="${engine.getNotesIn(engine.rootIndex)}">
                            <c:if test="${!engine.getNotesIn(index).contains(note)}">
                                <option value="${note.uuid}">${note.title}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    <button onclick="addEntry(document.getElementById('note-select').value)" type="button">Add Note</button>
                </div>
                </c:if>
            </div>
            <h3>Index Entries</h3>
            <div class="entries-indexes">
                <ol>
                    <c:forEach var="entry" items="${engine.getIndexesIn(index)}">
                        <c:if test="${!entry.equals(index)}">
                        <li>
                            <a href="/index/${entry.uuid}" class="entry-content">
                                <div class="entry-title">${entry.name}</div>
                                <div class="entry-brief">${entry.description}</div>
                            </a>
                            <c:if test="${!index.uuid.equals(engine.rootIndex.uuid)}">
                            <button class="remove-entry-btn" onclick="removeEntry('${entry.uuid}')" type="button">
                                Remove
                            </button>
                            </c:if>
                        </li>
                        </c:if>
                    </c:forEach>
                </ol>
                <c:if test="${!index.uuid.equals(engine.rootIndex.uuid)}">
                <div class="add-entry">
                    <h4>Add Index</h4>
                    <select id="index-select">
                        <option value="">Select an index to add</option>
                        <c:forEach var="idx" items="${engine.getIndexesIn(engine.rootIndex)}">
                            <c:if test="${!engine.getIndexesIn(index).contains(idx) && !idx.equals(index)}">
                                <option value="${idx.uuid}">${idx.name}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    <button onclick="addEntry(document.getElementById('index-select').value)" type="button">Add Index</button>
                </div>
                </c:if>
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
                    <textarea id="description" name="description" rows="3">${index.description}</textarea>
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
