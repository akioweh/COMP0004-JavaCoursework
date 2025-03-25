<%@ tag description="Renders a link element" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="element" required="true" type="com.akioweh.comp0004javacoursework.models.LinkElement" %>

<a href="${element.uri}" class="link-element" target="_blank" rel="noopener noreferrer">
    <c:out value="${element.displayText}" />
</a>