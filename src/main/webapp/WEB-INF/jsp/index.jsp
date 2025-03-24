<%--
  Author: akioweh
  24/03/2025 1:32 am
--%>
<%@ page import="com.akioweh.comp0004javacoursework.engine.Engine" %>
<%@ page import="com.akioweh.comp0004javacoursework.models.Note" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <jsp:include page="meta.jsp" />
    <title>Notes App | Home</title>
</head>
<body>
<header>
    <div class="container">
        <h1>Notes App</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
            </ul>
        </nav>
    </div>
</header>
<div class="container">
    <div class="main">
        <h2>Pinned</h2>
        <ol>
            <%
                List<Note> pinnedNotes = Engine.getInstance().getRootIndex()
                        .getNotes()
                        .stream()
                        .filter(note -> note.getTags().contains("pinned"))
                        .toList();
                for (Note note : pinnedNotes) {
            %>
            <li>
                <a href="note/<%= note.getUuid() %>}">
                    <%= note.getTitle() %>
                </a>
            </li>
            <%
                }

                if (pinnedNotes.isEmpty()) {
            %>
            <li>
                No pinned notes ~
                <i>Pin a note by adding the "pinned" tag.</i>
            </li>
            <%
                }
            %>
        </ol>
        <h2>
            <a href="index">
                All Notes
            </a>
        </h2>
    </div>
</div>
<jsp:include page="footer.jsp" />
</body>
</html>
