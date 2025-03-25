package com.akioweh.comp0004javacoursework.renderers;

import com.akioweh.comp0004javacoursework.models.MediaElement;
import org.jetbrains.annotations.NotNull;

import java.net.URI;


public class MediaRenderer implements ElementRenderer<MediaElement> {
    private static final MediaRenderer instance = new MediaRenderer();

    static MediaRenderer getInstance() {
        return instance;
    }

    @Override
    public @NotNull String render(@NotNull MediaElement element) {
        final URI url = element.getUri();
        final String caption = element.getDisplayText();
        switch (element.getMediaType()) {
            case IMAGE -> {
                return "<img src=\"" + url + "\" alt=\"" + caption + "\" />";
            }
            case VIDEO -> {
                return "<video controls>" +
                        "<source src=\"" + url + "\" type=\"video/mp4\" alt=\"" + caption + "\" >" +
                        "Your browser does not support the video tag." +
                        "</video>";
            }
            case AUDIO -> {
                return "<audio controls>" +
                        "<source src=\"" + url + "\" type=\"audio/mpeg\" alt=\"" + caption + "\" >" +
                        "Your browser does not support the audio tag." +
                        "</audio>";
            }
            default -> {  // Fallback to a link
                return "<a href=\"" + url + "\">" +
                        (caption.isBlank() ? url : caption) +
                        "</a>";
            }
        }
    }

    @Override
    public @NotNull String renderEdit(@NotNull MediaElement element) {
        var output = "<label for=\"mediaType\">Media Type:</label>" +
                "<select id=\"mediaType\" name=\"mediaType\" required>" +
                "<option value=\"IMAGE\">IMAGE</option>" +
                "<option value=\"VIDEO\">VIDEO</option>" +
                "<option value=\"AUDIO\">AUDIO</option>" +
                "</select>" +
                "<label for=\"uri\">URL:</label>" +
                "<input type=\"text\" id=\"uri\" name=\"uri\" value=\"" + element.getUri() + "\">" +
                "<label for=\"displayText\">Display Text:</label>\n" +
                "<input type=\"text\" id=\"displayText\" name=\"displayText\" value=\"" + element.getDisplayText() + "\">";
        var tmp = "\"" + element.getMediaType().name() + "\"";
        output = output.replaceFirst(tmp + ">", tmp + " selected>");
        return output;
    }
}
