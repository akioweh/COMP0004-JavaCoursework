<%--
  Author: akioweh
  24/03/2025 1:32 am
--%>
<%@ page import="com.akioweh.comp0004javacoursework.engine.Engine" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Home</title>
</head>
<body>
<header>
    <div class="container">
        <h1>Notes App</h1>
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
        <h2>Pinned</h2>
        <ol>
            <%
                var pinnedNotes = Engine.getInstance().getRootIndex()
                        .getNotes()
                        .stream()
                        .filter(note -> note.getTags().contains("pinned"))
                        .toList();
                for (var note : pinnedNotes) {
            %>
            <li>
                <a href="note/<%= note.getUuid() %>}">
                    <%= note.getTitle() %>
                </a>
            </li>
            <% } %>
            <% if (pinnedNotes.isEmpty()) { %>
            <li>
                No pinned notes ~
                <i>Pin a note by adding the "pinned" tag.</i>
            </li>
            <% } %>
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
