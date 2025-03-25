package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;


/**
 * Text element where the renderer should treat the content as HTML.
 * <p>
 * Follows JavaBeans conventions for better integration with JSP and servlet ecosystem.
 * </p>
 */
public class HTMLElement extends TextElement {
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
}
