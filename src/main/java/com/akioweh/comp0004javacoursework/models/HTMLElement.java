package com.akioweh.comp0004javacoursework.models;

import org.jetbrains.annotations.NotNull;

/**
 * Text element but where the renderer
 * should treat content as HTML.
 */
public class HTMLElement extends TextElement {
    public HTMLElement(@NotNull String html) {
        super(html);
    }
}
