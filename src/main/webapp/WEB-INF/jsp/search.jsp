<%--@elvariable id="engine" type="com.akioweh.comp0004javacoursework.engine.Engine"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="meta.jsp"/>
    <title>Notes App | Search</title>
</head>
<body>
<tags:header title="Search Notes" showNewIndex="false" />
<div class="container">
    <div class="main">
        <div class="search-container">
            <h2>Search Notes</h2>
            <p class="search-description">Search for notes by content, title, brief, or tag.</p>
            
            <form id="search-form" action="${pageContext.request.contextPath}/search" method="get">
                <div class="form-group">
                    <label for="search-term">Search Term:</label>
                    <input type="text" id="search-term" name="searchTerm" placeholder="Search in title, brief, and content">
                </div>
                <div class="form-group">
                    <label for="tag-filter">Filter by Tag:</label>
                    <input type="text" id="tag-filter" name="tag" placeholder="Enter a tag to filter by">
                </div>
                <div class="form-group">
                    <label for="sort-option">Sort By:</label>
                    <select id="sort-option" name="sortOption">
                        <option value="TITLE_ASC">Title (A-Z)</option>
                        <option value="TITLE_DESC">Title (Z-A)</option>
                        <option value="CREATED_ASC">Created (Oldest first)</option>
                        <option value="CREATED_DESC">Created (Newest first)</option>
                        <option value="MODIFIED_ASC">Modified (Oldest first)</option>
                        <option value="MODIFIED_DESC" selected>Modified (Newest first)</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="limit">Limit Results:</label>
                    <input type="number" id="limit" name="limit" placeholder="Leave empty for no limit" min="1">
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn-primary">Search</button>
                    <button type="reset" class="btn-secondary">Clear</button>
                </div>
            </form>

            <div class="popular-tags">
                <h3>Popular Tags</h3>
                <div class="tags-list">
                    <c:set var="allTags" value="${engine.allTags}" />
                    <c:forEach var="tag" items="${allTags}">
                        <a href="${pageContext.request.contextPath}/search?tag=${tag}" class="tag">${tag}</a>
                    </c:forEach>
                    <c:if test="${empty allTags}">
                        <p>No tags found. ~add tags to notes, and they'll show up here~</p>
                    </c:if>
                </div>
            </div>

            <div class="search-tips">
                <h3>Search Tips</h3>
                <ul>
                    <li>Search is case-insensitive</li>
                    <li>You can search in note titles, briefs, and content</li>
                    <li>For tag search, enter the exact tag name</li>
                    <li>You can combine search term and tag filter</li>
                    <li>Results are sorted by last modified date by default</li>
                </ul>
            </div>

        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>