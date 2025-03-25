<%--
  Author: akioweh
  24/03/2025 1:32 am
--%>
<%@ page import="com.akioweh.comp0004javacoursework.engine.Engine" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Home</title>
    <script>
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
    </script>
</head>
<body>
<header>
    <div class="container">
        <h1>Notes App</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><button onclick="newNote()">New Note</button></li>
                <li><button onclick="newIndex()">New Index</button></li>
            </ul>
        </nav>
    </div>
</header>
<div class="container">
    <div class="main">
        <h2>Pinned</h2>
        <ol>
            <c:set var="pinnedNotes" value="${Engine.getInstance().rootIndex.notes.stream().filter(note -> note.tags.contains('pinned')).toList()}" />
            <c:forEach var="note" items="${pinnedNotes}">
            <li>
                <a href="note/${note.uuid}">
                    ${note.title}
                </a>
            </li>
            </c:forEach>
            <c:if test="${empty pinnedNotes}">
            <li>
                No pinned notes ~
                <i>Pin a note by adding the "pinned" tag.</i>
            </li>
            </c:if>
        </ol>
        <h3>
            <a href="${pageContext.request.contextPath}/index">
                All Notes and Indexes
            </a>
        </h3>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
