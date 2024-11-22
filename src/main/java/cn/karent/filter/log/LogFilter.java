package cn.karent.filter.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * 记录输入输出的过滤器
 *
 * @author wanshengdao
 * @date 2024/6/13
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Order(value = 0)
public class LogFilter extends OncePerRequestFilter {

    private static final String LOG_LEVEL = "log_level";

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        BodyCachingRequestWrapper requestWrapper = new BodyCachingRequestWrapper(request);
        LogLevel logLevel = getLogLevel(request);
        StringBuilder logBody = new StringBuilder();
        // 记录请求行
        logRequestLine(request, logBody);
        // 记录请求头
        logRequestHeaderIfNeed(request, logBody, logLevel);
        // 记录请求体
        logRequestBodyIfNeed(logLevel, logBody, requestWrapper);

        log.info(logBody.toString());

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        byte[] body = responseWrapper.getContentAsByteArray();
        log.info("接口耗时: {}毫秒, 响应报文:{}", (System.currentTimeMillis() - start), new String(body, StandardCharsets.UTF_8));
        // 写回响应
        responseWrapper.copyBodyToResponse();
    }

    private static void logRequestBodyIfNeed(LogLevel logLevel, StringBuilder logBody, BodyCachingRequestWrapper requestWrapper) {
        if (logLevel.isLogBody()) {
            logBody.append("\n")
                    .append(new String(requestWrapper.getRequestBody(), StandardCharsets.UTF_8));
        }
    }

    /**
     * 获取当前日志级别
     *
     * @param request 请求对象
     * @return 日志级别
     */
    private static LogLevel getLogLevel(@NotNull HttpServletRequest request) {
        return LogLevel.of(request.getParameter(LOG_LEVEL));
    }

    /**
     * 日志记录请求行
     *
     * @param request 请求对象
     * @param logBody 日志内容
     */
    private static void logRequestLine(@NotNull HttpServletRequest request, StringBuilder logBody) {
        logBody.append(request.getMethod()).append(" ").append(request.getRequestURI());
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuilder params = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            params.append("&").append(key).append("=").append(value);
        }
        if (!params.isEmpty()) {
            logBody.append("?").append(params.substring(1));
        }
    }


    /**
     * 打印header
     *
     * @param request  请求对象
     * @param logBody  日志体
     * @param logLevel 日志级别
     */
    private static void logRequestHeaderIfNeed(@NotNull HttpServletRequest request, StringBuilder logBody, LogLevel logLevel) {
        if (logLevel.isLogHeader()) {
            logBody.append("\n");
            Enumeration<String> headerNames = request.getHeaderNames();
            StringBuilder sb = new StringBuilder();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String value = request.getHeader(key);
                sb.append(String.format("%s: %s\n", key, value));
            }
            logBody.append(sb);
        }
    }
}
