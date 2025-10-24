package org.example.dimollbackend.file.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class S3KeyUtils {

    private static final String KEY_FORMAT = "%s_%s";

    public static String generateKey(String fileName) {
        return KEY_FORMAT.formatted(UUID.randomUUID(), fileName);
    }
}
