package com.akioweh.comp0004javacoursework.renderers;


import com.akioweh.comp0004javacoursework.models.HTMLElement;
import com.akioweh.comp0004javacoursework.models.LinkElement;
import com.akioweh.comp0004javacoursework.models.MediaElement;
import com.akioweh.comp0004javacoursework.models.NoteElement;
import com.akioweh.comp0004javacoursework.models.TextElement;
import org.jetbrains.annotations.NotNull;


/**
 * Automatically picks the correct renderer for a given NoteElement
 * and renders it.
 */
public class AutoRenderer {
    public static @NotNull String render(@NotNull NoteElement element) {
        return switch (element) {  // switch is safe due to sealed classes
            case HTMLElement htmlElement -> HTMLRenderer.getInstance().render(htmlElement);
            case TextElement textElement -> TextRenderer.getInstance().render(textElement);
            case MediaElement mediaElement -> MediaRenderer.getInstance().render(mediaElement);
            case LinkElement linkElement -> LinkRenderer.getInstance().render(linkElement);
        };
    }

    public static @NotNull String renderEdit(@NotNull NoteElement element) {
        var formInner = switch (element) {
            case HTMLElement htmlElement -> HTMLRenderer.getInstance().renderEdit(htmlElement);
            case TextElement textElement -> TextRenderer.getInstance().renderEdit(textElement);
            case MediaElement mediaElement -> MediaRenderer.getInstance().renderEdit(mediaElement);
            case LinkElement linkElement -> LinkRenderer.getInstance().renderEdit(linkElement);
        };
        return "<form method=\"POST\" action=\"\">" +
                "<input type=\"hidden\" name=\"id\" value=\"" + element.getUuid() + "\">" +
                formInner +
                "<input type=\"submit\" value=\"Save\">" +
                "</form>";
    }
}
