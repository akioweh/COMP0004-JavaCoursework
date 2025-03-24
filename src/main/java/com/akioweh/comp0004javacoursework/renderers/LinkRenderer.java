package com.akioweh.comp0004javacoursework.renderers;

import com.akioweh.comp0004javacoursework.models.LinkElement;
import org.jetbrains.annotations.NotNull;


public class LinkRenderer implements ElementRenderer<LinkElement> {
    private static final LinkRenderer instance = new LinkRenderer();

    static LinkRenderer getInstance() {
        return instance;
    }

    @Override
    public @NotNull String render(@NotNull LinkElement element) {
        return "<a href=\"" + element.getUri() + "\">" + element.getDisplayText() + "</a>";
    }
}
