package com.akioweh.comp0004javacoursework.renderer;

import com.akioweh.comp0004javacoursework.models.HTMLElement;
import com.akioweh.comp0004javacoursework.models.LinkElement;
import com.akioweh.comp0004javacoursework.models.MediaElement;
import com.akioweh.comp0004javacoursework.models.NoteElement;
import com.akioweh.comp0004javacoursework.models.TextElement;
import org.jetbrains.annotations.NotNull;


/**
 * gets the correct renderer for a given element type.
 */
public class RendererFactory {
    @SuppressWarnings("unchecked")  // we know the type is correct
    public static <T extends NoteElement> @NotNull ElementRenderer<T> getRenderer(Class<T> elementClass) {
        if (TextElement.class.isAssignableFrom(elementClass)) {
            return (ElementRenderer<T>) TextRenderer.getInstance();
        } else if (HTMLElement.class.isAssignableFrom(elementClass)) {
            return (ElementRenderer<T>) HTMLRenderer.getInstance();
        } else if (LinkElement.class.isAssignableFrom(elementClass)) {
            return (ElementRenderer<T>) LinkRenderer.getInstance();
        } else if (MediaElement.class.isAssignableFrom(elementClass)) {
            return (ElementRenderer<T>) MediaRenderer.getInstance();
        } else {
            throw new IllegalArgumentException("No renderer found for element type: " + elementClass);
        }
    }
}
