package com.chitova.florist.logging.masking;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KeyValueMasking {
    public static void mask(final Map<String, String> values) {
        if (values != null) {
            values.entrySet().stream().filter((entry) -> MaskingCommon.isKeyToMask((String)entry.getKey())).forEach((entry) -> entry.setValue("*****"));
        }

    }

    public static Collection<String> mask(final String key, final Collection<String> values) {
        return (Collection<String>)(MaskingCommon.isKeyToMask(key) ? List.of("*****") : values);
    }
}