package com.akioweh.comp0004javacoursework.util;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;


public class Util {
    public static @Nullable UUID parseUUID(String uuidString) {
        if (uuidString == null || uuidString.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
