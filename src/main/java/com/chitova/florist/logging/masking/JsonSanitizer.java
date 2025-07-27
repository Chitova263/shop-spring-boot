package com.chitova.florist.logging.masking;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class JsonSanitizer {
    private static final Logger logger = LoggerFactory.getLogger(JsonSanitizer.class);
    private final JsonMasking jsonMasking;

    public JsonSanitizer() {
        this.jsonMasking = new JsonMasking((Collection)null);
    }

    public String sanitize(final String jsonString, final boolean returnOriginalOnSanitizationError) {
        return StringUtils.hasText(jsonString) ? this.mask(jsonString, returnOriginalOnSanitizationError) : jsonString;
    }

    private String mask(final String jsonString, final boolean returnOriginalOnSanitizationError) {
        String result;
        try {
            result = this.jsonMasking.maskJson(jsonString);
        } catch (JsonProcessingException | RuntimeException e) {
            logger.warn("Unable to sanitize input", e);
            if (returnOriginalOnSanitizationError) {
                result = jsonString;
            } else {
                result = "n/a";
            }
        }

        return result;
    }
}