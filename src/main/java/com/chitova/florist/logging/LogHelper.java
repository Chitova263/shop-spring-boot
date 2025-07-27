package com.chitova.florist.logging;

import org.springframework.core.env.PropertyResolver;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class LogHelper {
    public static boolean isLogEnabled(final PropertyResolver propertyResolver) {
        if (Boolean.TRUE.equals(propertyResolver.getProperty("logging.rest", Boolean.class, false))) {
            return true;
        }
        return false;
    }

    public static Map<String, String> flatten(final Map<String, List<String>> multiValueMap) {
        return (Map)multiValueMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, (entry) -> String.join(",", (Iterable)entry.getValue())));
    }

    public static void appendKeyValueMap(final Map<String, String> keyValueMap, final StringBuilder sb, final String name) {
        if (!keyValueMap.isEmpty()) {
            sb.append(" ").append(name).append("={");
            sb.append((String)keyValueMap.entrySet().stream().map((entry) -> {
                String var10000 = (String)entry.getKey();
                return var10000 + "=" + (String)entry.getValue();
            }).collect(Collectors.joining(", ")));
            sb.append("}");
        }

    }

    public static Charset getCharset(final MediaType mediaType) {
        Charset charset = null;
        if (mediaType != null) {
            charset = mediaType.getCharset();
        }
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        return charset;
    }
}
