<%--@elvariable id="index" type="com.akioweh.comp0004javacoursework.models.Index"--%>
<%--@elvariable id="engine" type="com.akioweh.comp0004javacoursework.engine.Engine"--%>
<%--@elvariable id="searchTerm" type="java.lang.String"--%>
<%--@elvariable id="tag" type="java.lang.String"--%>
<%--@elvariable id="sortOption" type="com.akioweh.comp0004javacoursework.util.NoteFilterSorter.SortOption"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Search Results</title>
</head>
<body>
<tags:header title="Search Results" showNewIndex="false" />
<div class="container">
    <div class="main">
        <div id="search-results-view">
            <div class="index-details">
                <h2>${index.name}</h2>
                <p><strong>Description:</strong> ${index.description}</p>
                
                <c:if test="${searchTerm != null && !searchTerm.isEmpty()}">
                <p><strong>Search Term:</strong> ${searchTerm}</p>
                </c:if>
                
                <c:if test="${tag != null && !tag.isEmpty()}">
                <p><strong>Tag:</strong> ${tag}</p>
                </c:if>
                
                <c:if test="${sortOption != null}">
                <p><strong>Sort By:</strong> ${sortOption}</p>
                </c:if>
                
                <p><a href="${pageContext.request.contextPath}/search" class="btn btn-secondary">New Search</a></p>
            </div>
            
            <h3>Results:</h3>
            <div class="entries-notes">
                <ol>
                    <c:forEach var="entry" items="${engine.getNotesIn(index)}">
                    <li>
                        <a href="${pageContext.request.contextPath}/note/${entry.uuid}" class="entry-content">
                            <div class="entry-title">${entry.title}</div>
                            <div class="entry-brief">${entry.brief}</div>
                        </a>
                    </li>
                    </c:forEach>
                </ol>
                <c:if test="${empty engine.getNotesIn(index)}">
                <div class="empty-notes-message">
                    <p>No results found</p>
                    <p class="empty-notes-hint">Try a different search term or tag.</p>
                </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>