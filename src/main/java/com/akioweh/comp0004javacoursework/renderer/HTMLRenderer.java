package com.akioweh.comp0004javacoursework.renderer;

import com.akioweh.comp0004javacoursework.models.HTMLElement;
import org.jetbrains.annotations.NotNull;


public class HTMLRenderer implements ElementRenderer<HTMLElement> {
    private static final HTMLRenderer instance = new HTMLRenderer();

    static HTMLRenderer getInstance() {
        return instance;
    }

    @Override
    public @NotNull String render(@NotNull HTMLElement element) {
        // basically no-op :)
        return "<div>" + element.getContent() + "</div>";
    }
}
