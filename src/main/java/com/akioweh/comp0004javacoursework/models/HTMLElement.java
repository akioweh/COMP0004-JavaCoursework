package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

/**
 * Text element where the renderer
 * should treat the content as HTML.
 */
public class HTMLElement extends TextElement {
    public HTMLElement(@NotNull String html) {
        super(html);
    }
}
