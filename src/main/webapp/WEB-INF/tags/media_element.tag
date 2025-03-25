<%@ tag description="Renders a media element" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="element" required="true" type="com.akioweh.comp0004javacoursework.models.MediaElement" %>

<div class="media-element">
    <c:choose>
        <c:when test="${element.mediaType == 'IMAGE'}">
            <img src="${element.uri}" alt="${element.displayText}" class="media-image" />
            <c:if test="${not empty element.displayText}">
                <div class="media-caption">${element.displayText}</div>
            </c:if>
        </c:when>
        <c:when test="${element.mediaType == 'VIDEO'}">
            <video controls class="media-video">
                <source src="${element.uri}" type="video/mp4">
                Your browser does not support the video tag.
            </video>
            <c:if test="${not empty element.displayText}">
                <div class="media-caption">${element.displayText}</div>
            </c:if>
        </c:when>
        <c:when test="${element.mediaType == 'AUDIO'}">
            <audio controls class="media-audio">
                <source src="${element.uri}" type="audio/mpeg">
                Your browser does not support the audio tag.
            </audio>
            <c:if test="${not empty element.displayText}">
                <div class="media-caption">${element.displayText}</div>
            </c:if>
        </c:when>
        <c:otherwise>
            <a href="${element.uri}" target="_blank" rel="noopener noreferrer" class="media-link">
                <c:choose>
                    <c:when test="${not empty element.displayText}">
                        ${element.displayText}
                    </c:when>
                    <c:otherwise>
                        ${element.uri}
                    </c:otherwise>
                </c:choose>
            </a>
        </c:otherwise>
    </c:choose>
</div>