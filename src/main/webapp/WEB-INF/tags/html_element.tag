<%@ tag description="Renders an HTML element" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="element" required="true" type="com.akioweh.comp0004javacoursework.models.HTMLElement" %>

<div class="html-element">
    <iframe srcdoc="${element.content}" style="width: 100%; border: none; overflow: hidden;"
            onload="resizeIframe(this)"></iframe>
</div>