<%--
  Author: akioweh
  24/03/2025 2:27 am
--%>
<%--@elvariable id="note" type="com.akioweh.comp0004javacoursework.models.Note"--%>
<%@ page import="com.akioweh.comp0004javacoursework.models.Note" %>
<%@ page import="com.akioweh.comp0004javacoursework.renderers.AutoRenderer" %>
<%@ page import="java.util.UUID" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    var note = (Note) request.getAttribute("note");
    var editTargetUuid = (UUID) request.getAttribute("editTargetUuid");
    var editMode = editTargetUuid != null;
%>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <script>
        function addNewElement(beforeElementUuid, elementType) {
            // send POST request to add new element
            fetch('${pageContext.request.contextPath}/addElement', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    elementType: elementType,
                    noteUuid: '${note.uuid}',
                    beforeElementUuid: beforeElementUuid
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
        <h2>${note.title}</h2>
        <hr>
        <div id="note-pre">
            <div id="date-details">
                <p><strong>Created:</strong> ${note.createdDate}</p>
                <p><strong>Last Modified:</strong> ${note.modifiedDate}</p>
            </div>
            <div id="tags-list">
                <ul>
                    <% for (var tag : note.getTags().stream().sorted().toList()) { %>
                    <li>
                        <%= tag %>
                    </li>
                    <% } %>
                </ul>
            </div>
            <div id="note-brief">
                <p><i>${note.brief}</i></p>
            </div>
        </div>
        <hr>
        <div id="note-content">
            <% for (var element : note.getElements()) {
                if (!editMode) { %>
            <div class="add-element-container">
                <button class="add-element-btn" onclick="addNewElement('<%= element.getUuid() %>', 'text')" type="button">
                    Add Text Element
                </button>
                <button class="add-element-btn" onclick="addNewElement('<%= element.getUuid() %>', 'html')" type="button">
                    Add HTML Element
                </button>
                <button class="add-element-btn" onclick="addNewElement('<%= element.getUuid() %>', 'link')" type="button">
                    Add Link Element
                </button>
                <button class="add-element-btn" onclick="addNewElement('<%= element.getUuid() %>', 'media')" type="button">
                    Add Media Element
                </button>
            </div>
            <% } %>
            <div class="note-element">
                <%= element.getUuid().equals(editTargetUuid) ? AutoRenderer.renderEdit(element) : AutoRenderer.render(element) %>
            </div>
            <% } %>
            <% if (true) { %>
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
            <% } %>
        </div>

    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
