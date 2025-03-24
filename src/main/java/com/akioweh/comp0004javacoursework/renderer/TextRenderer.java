package com.akioweh.comp0004javacoursework.renderer;

import com.akioweh.comp0004javacoursework.models.TextElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Helps split text up into lines and paragraphs.
 * This is useful for stuffing text into HTML elements.
 */
public class TextRenderer implements ElementRenderer<TextElement> {
    private static final TextRenderer instance = new TextRenderer();

    static TextRenderer getInstance() {
        return instance;
    }

    /**
     * Splits a paragraph into lines
     */
    private static String @NotNull [] asLines(@NotNull String paragraph) {
        return paragraph.split("(\r\n|\r|\n)");
    }

    /**
     * Splits text into paragraphs defined by TWO+ consecutive newlines (1+ blank line).
     */
    private static @NotNull String @NotNull [] asParagraphs(@NotNull TextElement noteElement) {
        return noteElement.getContent().split("(\r\n|\r|\n){2,}");
    }

    @Override
    public @NotNull String render(@NotNull TextElement element) {
        // each paragraph is in a <p> tag
        // lines in a paragraph are separated by <br> tags
        return Stream.of(asParagraphs(element))
                .map(paragraph -> "<p>" + String.join("<br>", List.of(asLines(paragraph))) + "</p>")
                .collect(Collectors.joining("\n"));
    }
}
