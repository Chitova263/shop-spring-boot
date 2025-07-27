package com.chitova.florist.logging;

import com.chitova.florist.logging.masking.JsonSanitizer;
import com.chitova.florist.logging.masking.KeyValueMasking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.env.PropertyResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class WebClientLogger {
    public static final Logger logger = LoggerFactory.getLogger(WebClientLogger.class);
    private final PropertyResolver propertyResolver;
    private final Map<String, String> contextMap;
    private HttpMethod httpMethod;
    private URI uri;
    private final Map<String, List<String>> requestHeaders;
    private String requestBody;
    private HttpStatusCode httpStatusCode;
    private final Map<String, List<String>> responseHeaders;
    private String responseBody;

    public WebClientLogger(final PropertyResolver propertyResolver, final Map<String, String> contextMap) {
        this.requestHeaders = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.responseHeaders = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.propertyResolver = propertyResolver;
        this.contextMap = contextMap;
    }

    public WebClientLogger httpMethod(final HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public WebClientLogger uri(final URI uri) {
        this.uri = uri;
        return this;
    }

    public WebClientLogger requestHeaders(final HttpHeaders headers) {
        this.requestHeaders.putAll(headers);
        return this;
    }

    public WebClientLogger requestBody(final byte[] body, final Charset charset) {
        this.requestBody = new String(body, charset);
        return this;
    }

    public WebClientLogger statusCode(final HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
        return this;
    }

    public WebClientLogger responseHeaders(final HttpHeaders headers) {
        this.responseHeaders.putAll(headers);
        return this;
    }

    public WebClientLogger responseBody(final String responseBody) {
        if (this.responseBody != null) {
            this.responseBody = this.responseBody + responseBody;
        } else {
            this.responseBody = responseBody;
        }

        return this;
    }

    public void writeLog() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request: httpMethod=").append(this.httpMethod).append(" url=").append(UriComponentsBuilder.fromUri(this.uri).replaceQuery((String)null).build().toUri());
        Map<String, String> queryParams = LogHelper.flatten(UriComponentsBuilder.fromUri(this.uri).build().getQueryParams());
        KeyValueMasking.mask(queryParams);
        LogHelper.appendKeyValueMap(queryParams, sb, "queryParams");
        Map<String, String> headerMap = LogHelper.flatten(this.requestHeaders);
        KeyValueMasking.mask(headerMap);
        LogHelper.appendKeyValueMap(headerMap, sb, "requestHeaders");
        if (this.requestBody != null && (this.isDocumentLogEnabled() || shouldLogPayload(headerMap))) {
            String sanitizedBody = (new JsonSanitizer().sanitize(this.requestBody, this.returnOriginalOnSanitizationError()));
            sb.append(" requestBody=").append(sanitizedBody);
        }

        if (this.httpStatusCode != null) {
            sb.append(" Response:").append(" statusCode=").append(this.httpStatusCode.value());
            Map<String, String> responseHeaderMap = LogHelper.flatten(this.responseHeaders);
            KeyValueMasking.mask(responseHeaderMap);
            LogHelper.appendKeyValueMap(responseHeaderMap, sb, "responseHeaders");
            if (StringUtils.hasLength(this.responseBody)) {
                String sanitizedBody = (new JsonSanitizer().sanitize(this.responseBody, this.returnOriginalResponseOnSanitizationError()));
                sb.append(" responseBody=").append(sanitizedBody);
            }
        }

        MDC.setContextMap(this.contextMap);
        logger.info("REST {}", sb);
    }

    private static boolean shouldLogPayload(final Map<String, String> headerMap) {
        String value = (String)headerMap.get("Content-Type");
        if (value != null) {
            return !value.toLowerCase().contains("multipart/form-data");
        } else {
            return true;
        }
    }

    private boolean returnOriginalResponseOnSanitizationError() {
        return !this.httpStatusCode.is2xxSuccessful() || this.returnOriginalOnSanitizationError();
    }

    private boolean returnOriginalOnSanitizationError() {
        return (Boolean)this.propertyResolver.getProperty("logging.rest.log-invalid-json", Boolean.class, false);
    }

    private boolean isDocumentLogEnabled() {
        return (Boolean)this.propertyResolver.getProperty("logging.rest.document", Boolean.class, true);
    }
}
