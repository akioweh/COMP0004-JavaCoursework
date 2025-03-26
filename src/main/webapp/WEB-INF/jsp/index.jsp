<%--@elvariable id="engine" type="com.akioweh.comp0004javacoursework.engine.Engine"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="com.akioweh.comp0004javacoursework.util.NoteFilterSorter" %>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Home</title>
</head>
<body>
<tags:header title="Notes App" showNewIndex="true" />
<div class="container home-container">
    <div class="main">
        <div class="welcome-section">
            <h1>Welcome to Notes App</h1>
            <p class="welcome-text">Your personal space for organizing thoughts, ideas, and information.</p>
        </div>

        <div class="home-section tags-section">
            <h2>Browse by Tags</h2>
            <div class="tags-scroll-container">
                <div class="tags-scroll-list">
                    <c:set var="allTags" value="${engine.allTags}" />
                    <c:forEach var="tag" items="${allTags}">
                        <a href="${pageContext.request.contextPath}/search?tag=${tag}" class="tag">${tag}</a>
                    </c:forEach>
                    <c:if test="${empty allTags}">
                        <p class="empty-tags-message">No tags found</p>
                    </c:if>
                </div>
            </div>
        </div>

        <div class="home-section recent-section">
            <h2>Recent Notes</h2>
            <div class="notes-grid">
                <c:set var="allNotes" value="${engine.getNotesIn(engine.rootIndex)}" />
                <c:set var="recentNotes" value="${NoteFilterSorter.getRecentNotes(allNotes, 6)}" />
                <c:forEach var="note" items="${recentNotes}">
                <div class="note-card">
                    <a href="${pageContext.request.contextPath}/note/${note.uuid}">
                        <div class="note-card-content">
                            <h3 class="note-card-title">${note.title}</h3>
                            <p class="note-card-brief">${note.brief}</p>
                            <div class="note-card-date">
                                <fmt:formatDate value="${note.modified}" pattern="MMM d, yyyy" />
                            </div>
                        </div>
                    </a>
                </div>
                </c:forEach>
                <c:if test="${empty recentNotes}">
                <div class="empty-notes-message">
                    <p>No notes yet</p>
                    <p class="empty-notes-hint">Create a new note using the button in the header.</p>
                </div>
                </c:if>
            </div>
        </div>

        <div class="home-section pinned-section">
            <h2>Pinned Notes</h2>
            <div class="pinned-notes-grid">
                <c:set var="pinnedNotes" value="${engine.getNotesIn(engine.rootIndex).stream().filter(note -> note.tags.contains('pinned')).toList()}" />
                <c:forEach var="note" items="${pinnedNotes}">
                <div class="note-card">
                    <a href="${pageContext.request.contextPath}/note/${note.uuid}">
                        <div class="note-card-content">
                            <h3 class="note-card-title">${note.title}</h3>
                            <p class="note-card-brief">${note.brief}</p>
                        </div>
                    </a>
                </div>
                </c:forEach>
                <c:if test="${empty pinnedNotes}">
                <div class="empty-notes-message">
                    <p>No pinned notes yet</p>
                    <p class="empty-notes-hint">Pin a note by adding the "pinned" tag.</p>
                </div>
                </c:if>
            </div>
        </div>

        <div class="home-section">
            <a href="${pageContext.request.contextPath}/index" class="view-all-button">
                <span>View All Notes and Indexes</span>
                <span class="arrow-icon">→</span>
            </a>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
