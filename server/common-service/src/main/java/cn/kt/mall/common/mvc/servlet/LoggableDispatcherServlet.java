package cn.kt.mall.common.mvc.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;


public class LoggableDispatcherServlet  extends DispatcherServlet {
    private static final long serialVersionUID = -7467646411065597887L;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        startTime.set(System.currentTimeMillis());

        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        HandlerExecutionChain handler = getHandler(request);

        try {
            super.doDispatch(request, response);
        } finally {
            log(request, response, handler);
            updateResponse(response);
        }
    }

    private void log(HttpServletRequest requestToCache, HttpServletResponse responseToCache, HandlerExecutionChain handler) {
        long start = startTime.get();
        long end = System.currentTimeMillis();

        String method = StringUtils.upperCase(requestToCache.getMethod());
        StringBuilder header = new StringBuilder();
        Enumeration<String> names = requestToCache.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            header.append(name).append("=").append(requestToCache.getHeader(name)).append(",");
        }

        StringBuilder responseHeader = new StringBuilder();
        Collection<String> responseNames = responseToCache.getHeaderNames();
        for (String name : responseNames) {
            responseHeader.append(name).append("=").append(responseToCache.getHeader(name)).append(",");
        }

        logger.info("\n Request Access Log:\n {} {} \n [ \n\t query:{} \n\t header:{} \n\t body:{} \n\t responseHeader:{} \n\t responseBody:{}\n]\n user times:{}ms",
                method, requestToCache.getRequestURI(), requestToCache.getQueryString(),
                header.length() > 0 ? header.substring(0, header.length() - 1) : null,
                "POST|PUT".contains(method) ? getRequestBody(requestToCache) : null,
                responseHeader.length() > 0 ? responseHeader.substring(0, responseHeader.length() - 1) : null,
                this.getResponsePayload(responseToCache),
                (end - start));
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            ContentCachingRequestWrapper requestWrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
            byte[] data = requestWrapper.getContentAsByteArray();
            return new String(data, requestWrapper.getCharacterEncoding());
        } catch (IOException e) {
            logger.error("网管前置校验错误，错误原因：" + e.getMessage(), e);
        }

        return "";
    }

    private String getResponsePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            if(response.getContentType() == null || !response.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                return "[unknown]";
            }
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, 5120);
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                }
                catch (UnsupportedEncodingException ex) {
                    // NOOP
                }
            }
        }
        return "[unknown]";
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        responseWrapper.copyBodyToResponse();
    }

}