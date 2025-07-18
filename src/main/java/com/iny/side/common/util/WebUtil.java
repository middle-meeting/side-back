package com.iny.side.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtil {

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    private static final String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";

    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }

    public static boolean isContentTypeJson(HttpServletRequest request) {
        String header = request.getHeader(CONTENT_TYPE);
        return header != null && header.contains(CONTENT_TYPE_JSON);
    }
}
