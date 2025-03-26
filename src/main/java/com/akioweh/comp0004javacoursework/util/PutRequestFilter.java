package com.akioweh.comp0004javacoursework.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Filter that processes PUT requests to make form data available through the getParameter methods.
 * <p>
 * The Jakarta Servlet API only automatically parses form data for POST requests, not for PUT requests.
 * This filter reads the request body for PUT requests and parses it as form data, making it available
 * through the getParameter and getParameterMap methods.
 * </p>
 */
@WebFilter(urlPatterns = "/*")
public class PutRequestFilter implements Filter {
    private static final Logger logger = Logger.getLogger(PutRequestFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            String method = httpRequest.getMethod();
            String contentType = httpRequest.getContentType();

            // Only process PUT requests with form data
            if ("PUT".equalsIgnoreCase(method) && contentType != null && contentType.contains("application/x-www-form-urlencoded")) {
                // Wrap the request to provide access to form parameters
                request = new PutRequestWrapper(httpRequest);
            }
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }

    /**
     * Wrapper for HttpServletRequest that provides access to form parameters in PUT requests.
     */
    private static class PutRequestWrapper extends HttpServletRequestWrapper {
        private final Map<String, String[]> parameterMap;
        private final byte[] requestBody;

        public PutRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            // Read the request body
            requestBody = request.getInputStream().readAllBytes();

            // Parse the request body as form data
            parameterMap = parseFormData(new String(requestBody, StandardCharsets.UTF_8));

        }

        @Override
        public String getParameter(String name) {
            String[] values = parameterMap.get(name);
            return values != null && values.length > 0 ? values[0] : null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.unmodifiableMap(parameterMap);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(parameterMap.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return parameterMap.get(name);
        }

        @Override
        public ServletInputStream getInputStream() {
            return new ServletInputStream() {
                private final ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody);

                @Override
                public int read() {
                    return inputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return inputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    throw new UnsupportedOperationException("setReadListener is not supported");
                }
            };
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }

        /**
         * Parses form data from a URL-encoded string.
         *
         * @param formData The URL-encoded form data
         * @return A map of parameter names to parameter values
         */
        private Map<String, String[]> parseFormData(String formData) {
            Map<String, String[]> result = new HashMap<>();
            if (formData == null || formData.isEmpty()) {
                return result;
            }

            String[] pairs = formData.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf('=');
                String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8) : pair;
                String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8) : "";

                // Add the parameter to the map
                String[] existingValues = result.get(key);
                if (existingValues == null) {
                    result.put(key, new String[]{value});
                } else {
                    String[] newValues = Arrays.copyOf(existingValues, existingValues.length + 1);
                    newValues[existingValues.length] = value;
                    result.put(key, newValues);
                }
            }

            return result;
        }
    }
}
