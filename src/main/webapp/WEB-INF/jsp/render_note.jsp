<%--@elvariable id="note" type="com.akioweh.comp0004javacoursework.models.Note"--%>
<%--@elvariable id="noteViewState" type="com.akioweh.comp0004javacoursework.view.NoteViewState"--%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="note" value="${noteViewState.note}" />
<c:set var="editMode" value="${noteViewState.editMode}" />
<c:set var="editTargetUuid" value="${noteViewState.editTargetUuid}" />
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <script>
        // Set the note UUID for note.js
        var noteUuid = '${note.uuid}';
    </script>
    <script src="${pageContext.request.contextPath}/static/js/note.js"></script>
    <title>Notes App | Note View</title>
</head>
<body>
<tags:header title="Note View" showNewIndex="false" />
<div class="container">
    <div id="main">
        <div id="note-view">
            <div class="note-actions">
                <button onclick="editNote()">Edit Details</button>
                <button onclick="deleteNote()">Delete Note</button>
            </div>
            <h2>${note.title}</h2>
            <hr>
            <div id="note-pre">
                <div id="date-details">
                    <p><strong>Created:</strong> ${note.createdDate}</p>
                    <p><strong>Last Modified:</strong> ${note.modifiedDate}</p>
                </div>
                <div id="tags-list">
                    <h3>Tags:</h3>
                    <ul>
                        <c:choose>
                            <c:when test="${empty note.tags}">
                                <p class="empty-tags-message">none</p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="tag" items="${note.tags}">
                                <li>
                                    ${tag}
                                </li>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
                <div id="note-brief">
                    <p><i>${note.brief}</i></p>
                </div>
            </div>
            <hr>
        </div>

        <div id="note-edit" style="display: none;">
            <h2>Edit Note</h2>
            <form id="edit-note-form" onsubmit="updateNote(event)">
                <div class="form-group">
                    <label for="title">Title:</label>
                    <input type="text" id="title" name="title" value="${note.title}" required>
                </div>
                <div class="form-group">
                    <label for="brief">Brief Description:</label>
                    <textarea id="brief" name="brief" rows="3">${note.brief}</textarea>
                </div>
                <div class="form-group">
                    <label for="tags">Tags (comma separated):</label>
                    <input type="text" id="tags" name="tags" value="${fn:join(note.tags.toArray(), ', ')}">
                </div>
                <div class="form-actions">
                    <button type="submit">Save Changes</button>
                    <button type="button" onclick="cancelEdit()">Cancel</button>
                </div>
            </form>
        </div>


        <div id="note-content">
            <c:forEach var="element" items="${note.elements}">
                <c:if test="${not editMode}">
                <div class="add-element-container">
                    <div class="button-container">
                        <button class="add-element-btn" onclick="addNewElement('${element.uuid}', 'text')" type="button">
                            Add Text Element
                        </button>
                        <button class="add-element-btn" onclick="addNewElement('${element.uuid}', 'html')" type="button">
                            Add HTML Element
                        </button>
                        <button class="add-element-btn" onclick="addNewElement('${element.uuid}', 'link')" type="button">
                            Add Link Element
                        </button>
                        <button class="add-element-btn" onclick="addNewElement('${element.uuid}', 'media')" type="button">
                            Add Media Element
                        </button>
                    </div>
                </div>
                </c:if>
                <div class="note-element">
                    <div class="element-actions">
                        <button class="delete-element-btn" onclick="deleteElement('${element.uuid}')" type="button">
                            Delete Element
                        </button>
                        <c:if test="${not noteViewState.isElementBeingEdited(element)}">
                        <a href="${pageContext.request.contextPath}/note/${note.uuid}?edit=${element.uuid}" class="edit-element-btn">
                            Edit Element
                        </a>
                        </c:if>
                    </div>
                    <tags:element element="${element}" noteUuid="${note.uuid}" editMode="${noteViewState.isElementBeingEdited(element)}" />
                </div>
            </c:forEach>
            <div class="add-element-container">
                <div class="button-container">
                    <button class="add-element-btn" onclick="addNewElement(null, 'text')" type="button">
                        Add Text Element
                    </button>
                    <button class="add-element-btn" onclick="addNewElement(null, 'html')" type="button">
                        Add HTML Element
                    </button>
                    <button class="add-element-btn" onclick="addNewElement(null, 'link')" type="button">
                        Add Link Element
                    </button>
                    <button class="add-element-btn" onclick="addNewElement(null, 'media')" type="button">
                        Add Media Element
                    </button>
                </div>
            </div>
        </div>

    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
