package com.akioweh.comp0004javacoursework.renderer;

import com.akioweh.comp0004javacoursework.models.NoteElement;
import org.jetbrains.annotations.NotNull;


public interface ElementRenderer<T extends NoteElement> {
      @NotNull String render(@NotNull T element);

//    @NotNull String renderEdit(@NotNull T element);
}
