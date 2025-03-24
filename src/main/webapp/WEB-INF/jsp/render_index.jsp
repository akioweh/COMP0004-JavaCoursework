<%--
  Author: akioweh
  23/03/2025 7:35 am
--%>
<%--@elvariable id="obj" type="com.akioweh.comp0004javacoursework.models.Index"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.akioweh.comp0004javacoursework.models.Index" %>
<% var index = (Index) request.getAttribute("obj"); %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Index View</title>
</head>
<body>
<header>
    <div class="container">
        <h1>Index Details</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
            </ul>
        </nav>
    </div>
</header>
<div class="container">
    <div class="main">
        <div class="index-details">
            <h2>${obj.name}</h2>
            <p><strong>Description:</strong> ${obj.description}</p>
        </div>
        <h3>Note Entries:</h3>
        <div class="entries-notes">
            <ol>
                <% for (var entry : index.getNotes()) { %>
                <li>
                    <a href="note/<%= entry.getUuid() %>">
                        <%= entry.getTitle() %>
                    </a>
                </li>
                <% } %>
            </ol>
        </div>
        <h3>Index Entries</h3>
        <div class="entries-indexes">
            <ol>
                <%
                    for (var entry : index.getIndexes()) {
                        if (entry.equals(index)) continue;  // don't include self
                %>
                <li>
                    <a href="index/<%= entry.getUuid() %>">
                        <%= entry.getName() %>
                    </a>
                </li>
                <% } %>
            </ol>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
