package com.akioweh.comp0004javacoursework.renderer;

import com.akioweh.comp0004javacoursework.models.TextElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;


/**
 * Helps split text up into lines and paragraphs.
 * This is useful for stuffing text into HTML elements.
 */
public class TextRenderer {
    /**
     * Splits a paragraph into lines
     */
    public static class Paragraph {
        @NotNull
        private final String text;

        public Paragraph(@NotNull String text) {
            this.text = text;
        }

        public List<String> getLines() {
            return List.of(text.split("(\r\n|\r|\n)"));
        }
    }

    /**
     * Splits text into paragraphs defined by TWO+ consecutive newlines (1+ blank line).
     */
    public static @NotNull List<Paragraph> render(@NotNull TextElement noteElement) {
        var text = noteElement.getContent();
        var paragraphs = text.split("(\r\n|\r|\n){2,}");
        return Stream.of(paragraphs)
                .map(Paragraph::new)
                .toList();
    }
}
