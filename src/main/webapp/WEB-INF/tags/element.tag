<%@ tag description="Renders an element based on its type" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="element" required="true" type="com.akioweh.comp0004javacoursework.models.NoteElement" %>
<%@ attribute name="noteUuid" required="true" type="java.util.UUID" %>
<%@ attribute name="editMode" required="false" type="java.lang.Boolean" %>

<c:choose>
    <c:when test="${editMode}">
        <%-- Edit mode - render the appropriate edit tag --%>
        <c:choose>
            <c:when test="${element['class'].simpleName == 'TextElement'}">
                <tags:text_element_edit element="${element}" noteUuid="${noteUuid}" />
            </c:when>
            <c:when test="${element['class'].simpleName == 'HTMLElement'}">
                <tags:html_element_edit element="${element}" noteUuid="${noteUuid}" />
            </c:when>
            <c:when test="${element['class'].simpleName == 'LinkElement'}">
                <tags:link_element_edit element="${element}" noteUuid="${noteUuid}" />
            </c:when>
            <c:when test="${element['class'].simpleName == 'MediaElement'}">
                <tags:media_element_edit element="${element}" noteUuid="${noteUuid}" />
            </c:when>
            <c:otherwise>
                <div class="error">Unknown element type: ${element['class'].simpleName}</div>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <%-- View mode - render the appropriate view tag --%>
        <c:choose>
            <c:when test="${element['class'].simpleName == 'TextElement'}">
                <tags:text_element element="${element}" />
            </c:when>
            <c:when test="${element['class'].simpleName == 'HTMLElement'}">
                <tags:html_element element="${element}" />
            </c:when>
            <c:when test="${element['class'].simpleName == 'LinkElement'}">
                <tags:link_element element="${element}" />
            </c:when>
            <c:when test="${element['class'].simpleName == 'MediaElement'}">
                <tags:media_element element="${element}" />
            </c:when>
            <c:otherwise>
                <div class="error">Unknown element type: ${element['class'].simpleName}</div>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>