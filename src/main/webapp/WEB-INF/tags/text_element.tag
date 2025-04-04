<%@ tag description="Renders a text element" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ attribute name="element" required="true" type="com.akioweh.comp0004javacoursework.models.TextElement" %>

<%-- Split text into paragraphs defined by TWO+ consecutive newlines (1+ blank line) --%>
<c:set var="paragraphs" value='${element.content.split("((\\\\r?\\\\n){2})")}' />

<%-- Render each paragraph --%>
<c:forEach var="paragraph" items="${paragraphs}">
    <p>
        <%-- Split paragraph into lines and join with <br> tags --%>
        <c:set var="lines" value="${paragraph.split('(\\\\r?\\\\n)')}" />
        <c:forEach var="line" items="${lines}" varStatus="status">
            <c:out value="${line}" />
            <c:if test="${!status.last}"><br /></c:if>
        </c:forEach>
    </p>
</c:forEach>