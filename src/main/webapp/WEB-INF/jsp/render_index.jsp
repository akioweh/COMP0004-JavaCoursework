<%--
  Author: akioweh
  23/03/2025 7:35 am
--%>
<%--@elvariable id="index" type="com.akioweh.comp0004javacoursework.models.Index"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.akioweh.comp0004javacoursework.models.Index" %>
<%@ page import="com.akioweh.comp0004javacoursework.engine.Engine" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Index View</title>
    <script>
        function deleteIndex() {
            if (confirm('Are you sure you want to delete this index?')) {
                // send DELETE request to delete index
                fetch('${pageContext.request.contextPath}/api/index/${index.uuid}', {
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

        function editIndex() {
            document.getElementById('index-view').style.display = 'none';
            document.getElementById('index-edit').style.display = 'block';
        }

        function cancelEdit() {
            document.getElementById('index-view').style.display = 'block';
            document.getElementById('index-edit').style.display = 'none';
        }

        function removeEntry(entryUuid) {
            if (confirm('Are you sure you want to remove this entry from the index?')) {
                // send DELETE request to remove entry
                fetch('${pageContext.request.contextPath}/api/index/${index.uuid}/entry/' + entryUuid, {
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

        function newIndex() {
            // send POST request to create a new index
            fetch('${pageContext.request.contextPath}/api/index', {
                method: 'POST'
            }).then(response => {
                if (response.ok) {
                    // redirect to the new index
                    window.location.href = response.url;
                } else {
                    response.text().then(text => {
                        alert('Error: ' + text);
                    });
                }
            });
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
</head>
<body>
<header>
    <div class="container">
        <h1>Index Details</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><button onclick="newNote()">New Note</button> </li>
            </ul>
        </nav>
    </div>
</header>
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

        <script>
            function updateIndex(event) {
                event.preventDefault();
                const form = document.getElementById('edit-index-form');
                const formData = new FormData(form);

                // send PUT request to update index
                fetch('${pageContext.request.contextPath}/api/index/${index.uuid}', {
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
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
