<%--
  Author: akioweh 
  25/03/2025 10:55 am
--%>
<%@ tag description="Header component" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="title" required="true" type="java.lang.String" description="The title to display in the header" %>
<%@ attribute name="showNewIndex" required="false" type="java.lang.Boolean" description="Whether to show the New Index button" %>

<header>
    <div class="container">
        <h1>${title}</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><button onclick="newNote()">New Note</button></li>
                <c:if test="${showNewIndex}">
                    <li><button onclick="newIndex()">New Index</button></li>
                </c:if>
            </ul>
        </nav>
    </div>
</header>