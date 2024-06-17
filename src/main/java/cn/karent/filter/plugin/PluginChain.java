package cn.karent.filter.plugin;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;
import java.util.*;

/**
 * 插件调用链
 *
 * @author wanshengdao
 * @date 2024/6/16
 */
public class PluginChain {

    private final HttpServletRequest servletRequest;

    private final HttpServletResponse servletResponse;

    private final FilterChain filterChain;

    private final List<Plugin> plugins = new ArrayList<>();

    private int idx;

    public PluginChain(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) {
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
        this.filterChain = filterChain;
    }

    public void doProcess(Request request, Response response) throws ServletException, IOException {
        if (idx >= plugins.size()) {
            invokeUnsafeFilters(request, response);
        } else {
            plugins.get(idx++).doProcess(request, response, this);
        }
    }

    private void invokeUnsafeFilters(Request request, Response response) throws ServletException, IOException {
        // 构建请求
        BodyHttpServletRequestAdapter requestAdapter = new BodyHttpServletRequestAdapter(servletRequest, request.getBody());
        ContentCachingResponseWrapper responseAdapter = new ContentCachingResponseWrapper(servletResponse);
        filterChain.doFilter(requestAdapter, responseAdapter);
        // 拿到响应体
        response.setBody(responseAdapter.getContentAsByteArray());
        // 拿到响应头
        response.setHeaders(collectResponseHeaders(responseAdapter));
    }

    @NotNull
    private Map<String, String> collectResponseHeaders(ContentCachingResponseWrapper responseAdapter) {
        Map<String, String> headers = new HashMap<>();
        Collection<String> headerNames = responseAdapter.getHeaderNames();
        headerNames.forEach(k -> headers.put(k, responseAdapter.getHeader(k)));
        return headers;
    }

    /**
     * 添加插件
     *
     * @param plugin 插件
     */
    public void addPlugin(Plugin plugin) {
        if (Objects.isNull(plugin)) {
            return;
        }
        this.plugins.add(plugin);
    }

}
