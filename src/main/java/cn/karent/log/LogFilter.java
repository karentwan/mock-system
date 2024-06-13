package cn.karent.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 记录输入输出的过滤器
 *
 * @author wanshengdao
 * @date 2024/6/13
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        BodyCachingRequestWrapper requestWrapper = new BodyCachingRequestWrapper(request);
        log.info("接口请求报文: {}", new String(requestWrapper.getRequestBody(), StandardCharsets.UTF_8));

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        byte[] body = responseWrapper.getContentAsByteArray();
        log.info("接口耗时: {} 毫秒\t响应报文:{}", (System.currentTimeMillis() - start), new String(body, StandardCharsets.UTF_8));
        // 写回响应
        responseWrapper.copyBodyToResponse();
    }
}
