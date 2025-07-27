package com.chitova.florist.logging.masking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.CollectionUtils;

import java.util.*;

class JsonMasking {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Set<String> keysToMaskLowercase;

    public JsonMasking(final Collection<String> additionalKeysToMask) {
        if (CollectionUtils.isEmpty(additionalKeysToMask)) {
            this.keysToMaskLowercase = MaskingCommon.KEYS_TO_MASK_LOWERCASE;
        } else {
            Set<String> keys = new HashSet(MaskingCommon.KEYS_TO_MASK_LOWERCASE);
            keys.addAll(additionalKeysToMask);
            this.keysToMaskLowercase = Collections.unmodifiableSet(keys);
        }

    }

    public String maskJson(final String json) throws JsonProcessingException {
        String trimmedJson = json.trim();
        if (!trimmedJson.startsWith("{") && !trimmedJson.startsWith("[")) {
            return trimmedJson;
        } else {
            JsonNode node = this.mapper.readTree(json);
            this.maskJsonNode("", node, (JsonNode)null);
            return node.toString();
        }
    }

    private void maskJsonNode(final String key, final JsonNode node, final JsonNode parentNode) {
        if (node.isArray()) {
            this.maskIfNeeded(key, parentNode);
            Iterator<JsonNode> elements = node.elements();

            while(elements.hasNext()) {
                this.maskJsonNode("", (JsonNode)elements.next(), (JsonNode)null);
            }
        } else if (node.isObject()) {
            node.fields().forEachRemaining((entry) -> this.maskJsonNode((String)entry.getKey(), (JsonNode)entry.getValue(), node));
        } else {
            this.maskIfNeeded(key, parentNode);
        }

    }

    private void maskIfNeeded(final String key, final JsonNode parentNode) {
        if (this.keysToMaskLowercase.contains(key.toLowerCase())) {
            ((ObjectNode)parentNode).put(key, "*****");
        }

    }
}