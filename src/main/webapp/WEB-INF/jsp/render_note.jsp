<%--
  Author: akioweh
  24/03/2025 2:27 am
--%>
<%--@elvariable id="note" type="com.akioweh.comp0004javacoursework.models.Note"--%>
<%@ page import="com.akioweh.comp0004javacoursework.models.Note" %>
<%@ page import="com.akioweh.comp0004javacoursework.renderers.AutoRenderer" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% var note = (Note) request.getAttribute("note"); %>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Note View</title>
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
            <%
                for (var element : note.getElements()) {
            %>
            <div class="note-element">
                <%= AutoRenderer.render(element) %>
            </div>
            <% } %>
        </div>

    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
