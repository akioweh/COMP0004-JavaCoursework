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

    @Override
    public @NotNull String renderEdit(@NotNull LinkElement element) {
        return "<label for=\"uri\">URL:</label>" +
                "<input type=\"text\" id=\"uri\" name=\"uri\" value=\"" + element.getUri() + "\">" +
                "<label for=\"displayText\">Display Text:</label>" +
                "<input type=\"text\" id=\"displayText\" name=\"displayText\" value=\"" + element.getDisplayText() + "\">";
    }
}
