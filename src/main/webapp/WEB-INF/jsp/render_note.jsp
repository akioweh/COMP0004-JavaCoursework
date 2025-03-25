<%--
  Author: akioweh
  24/03/2025 2:27 am
--%>
<%--@elvariable id="note" type="com.akioweh.comp0004javacoursework.models.Note"--%>
<%@ page import="com.akioweh.comp0004javacoursework.models.Note" %>
<%@ page import="com.akioweh.comp0004javacoursework.renderers.AutoRenderer" %>
<%@ page import="java.util.UUID" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="editMode" value="${not empty editTargetUuid}" />
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <script>
        function addNewElement(beforeElementUuid, elementType) {
            // send POST request to add new element
            let url = '${pageContext.request.contextPath}/api/element/${note.uuid}';
            if (beforeElementUuid) {
                url += '/' + beforeElementUuid;
            }

            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    elementType: elementType
                }).toString()
            }).then(response => {
                if (response.ok) {
                    // reload the page to see the new element
                    location.reload();
                } else {
                    response.text().then(text => {
                        alert('Error: ' + text);
                    });
                }
            });
        }

        function deleteElement(elementUuid) {
            if (confirm('Are you sure you want to delete this element?')) {
                // send DELETE request to delete element
                fetch('${pageContext.request.contextPath}/api/element/${note.uuid}/' + elementUuid, {
                    method: 'DELETE'
                }).then(response => {
                    if (response.ok) {
                        // reload the page to see the changes
                        location.reload();
                    } else {
                        response.text().then(text => {
                            alert('Error: ' + text);
                        });
                    }
                });
            }
        }

        function deleteNote() {
            if (confirm('Are you sure you want to delete this note?')) {
                // send DELETE request to delete note
                fetch('${pageContext.request.contextPath}/api/note/${note.uuid}', {
                    method: 'DELETE'
                }).then(response => {
                    if (response.ok) {
                        // redirect to home page
                        window.location.href = '${pageContext.request.contextPath}/';
                    } else {
                        response.text().then(text => {
                            alert('Error: ' + text);
                        });
                    }
                });
            }
        }

        function editNote() {
            document.getElementById('note-view').style.display = 'none';
            document.getElementById('note-edit').style.display = 'block';
        }

        function cancelEdit() {
            document.getElementById('note-view').style.display = 'block';
            document.getElementById('note-edit').style.display = 'none';
        }

        function newNote() {
            // send POST request to create a new note
            fetch('${pageContext.request.contextPath}/api/note', {
                method: 'POST'
            }).then(response => {
                if (response.ok) {
                    // redirect to the new note
                    window.location.href = response.url;
                } else {
                    response.text().then(text => {
                        alert('Error: ' + text);
                    });
                }
            });
        }
    </script>
    <title>Notes App | Note View</title>
</head>
<body>
<header>
    <div class="container">
        <h1>Note View</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><button onclick="newNote()">New Note</button> </li>
            </ul>
        </nav>
    </div>
</header>
<div class="container">
    <div id="main">
        <div id="note-view">
            <div class="note-actions">
                <button onclick="editNote()">Edit Note</button>
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
                        <c:forEach var="tag" items="${note.tags}">
                        <li>
                            ${tag}
                        </li>
                        </c:forEach>
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
                    <textarea id="brief" name="brief" rows="3" required>${note.brief}</textarea>
                </div>
                <div class="form-group">
                    <label for="tags">Tags (comma separated):</label>
                    <input type="text" id="tags" name="tags" value="${fn:join(note.tags, ', ')}">
                </div>
                <div class="form-actions">
                    <button type="submit">Save Changes</button>
                    <button type="button" onclick="cancelEdit()">Cancel</button>
                </div>
            </form>
        </div>

        <script>
            function updateNote(event) {
                event.preventDefault();
                const form = document.getElementById('edit-note-form');
                const formData = new FormData(form);

                // send PUT request to update note
                fetch('${pageContext.request.contextPath}/api/note/${note.uuid}', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams(formData).toString()
                }).then(response => {
                    if (response.ok) {
                        // reload the page to see the changes
                        location.reload();
                    } else {
                        response.text().then(text => {
                            alert('Error: ' + text);
                        });
                    }
                });
            }
        </script>

        <div id="note-content">
            <c:forEach var="element" items="${note.elements}">
                <c:if test="${not editMode}">
                <div class="add-element-container">
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
                </c:if>
                <div class="note-element">
                    <div class="element-actions">
                        <button class="delete-element-btn" onclick="deleteElement('${element.uuid}')" type="button">
                            Delete Element
                        </button>
                        <c:if test="${not element.uuid.equals(editTargetUuid)}">
                        <a href="${pageContext.request.contextPath}/note/${note.uuid}?edit=${element.uuid}" class="edit-element-btn">
                            Edit Element
                        </a>
                        </c:if>
                    </div>
                    ${element.uuid.equals(editTargetUuid) ? AutoRenderer.renderEdit(element) : AutoRenderer.render(element)}
                </div>
            </c:forEach>
            <div class="add-element-container">
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
<jsp:include page="footer.jsp"/>
</body>
</html>
