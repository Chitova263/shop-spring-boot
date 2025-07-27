package com.chitova.florist.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
public class HttpRequestContextAccessor {

    public HttpServletRequest getCurrentRequest() {
        final ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            throw new IllegalStateException("No request context available (maybe outside a request)");
        }
        return servletRequestAttributes.getRequest();
    }

    public String getHeader(final String headerName) {
        return getCurrentRequest().getHeader(headerName);
    }

    public String getRemoteAddress() {
        return getCurrentRequest().getRemoteAddr();
    }

    public String getRequestURI() {
        return getCurrentRequest().getRequestURI();
    }
}



