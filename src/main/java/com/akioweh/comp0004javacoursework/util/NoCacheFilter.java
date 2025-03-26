package com.akioweh.comp0004javacoursework.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filter that adds cache control headers to all responses to disable browser caching.
 * <p>
 * This filter ensures that browsers always fetch the latest content from the server
 * instead of using cached versions. This is particularly important for dynamic content
 * that changes frequently.
 * </p>
 */
@WebFilter(urlPatterns = "/*")
public class NoCacheFilter implements Filter {
    private static final Logger logger = Logger.getLogger(NoCacheFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // First, continue with the filter chain
        chain.doFilter(request, response);

        // Then add cache control headers if it's an HTTP response
        // This ensures our headers won't be overridden by servlets in the chain
        if (response instanceof HttpServletResponse httpResponse) {
            // Set headers to disable caching
            httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setHeader("Expires", "0");
            httpResponse.setDateHeader("Last-Modified", System.currentTimeMillis());
        }
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
