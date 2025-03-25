<%--
  Author: akioweh
  24/03/2025 1:32 am
--%>
<%@ page import="com.akioweh.comp0004javacoursework.engine.Engine" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Home</title>
</head>
<body>
<tags:header title="Notes App" showNewIndex="true" />
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
