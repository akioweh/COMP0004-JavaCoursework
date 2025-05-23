package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;


/**
 * Text element where the renderer should treat the content as HTML.
 * <p>
 * Follows JavaBeans conventions for better integration with JSP and servlet ecosystem.
 * </p>
 */
public class HTMLElement extends TextElement {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor required for JavaBeans.
     */
    public HTMLElement() {
        super();
    }

    /**
     * Constructor with HTML content.
     * 
     * @param html The HTML content
     */
    public HTMLElement(@NotNull String html) {
        super(html);
    }

    /**
     * Extracts searchable text from this element.
     * For HTML elements, this strips HTML tags to get plain text.
     * 
     * @return The plain text content of this HTML element
     */
    @Override
    public @NotNull String extractSearchableText() {
        // Simple HTML tag stripping - could be improved with a proper HTML parser
        return getContent().replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
    }
}
