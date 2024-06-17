package cn.karent.filter.plugin;

import cn.karent.core.ConfigController;
import cn.karent.core.model.PluginConfig;
import cn.karent.core.storage.TemplateStorage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 插件组合过滤器, 这里会根据配置的插件模板对请求和响应进行处理
 *
 * @author wanshengdao
 * @date 2024/6/16
 */
@RequiredArgsConstructor
@Slf4j
@Component
@Order(1)
public class PluginCompositeFilter extends OncePerRequestFilter {

    /**
     * 该filter不处理的url列表
     */
    private final Set<String> notFilterUrls = Set.of(ConfigController.CONFIG_URL);

    private final List<HandlerExceptionResolver> handlerExceptionResolvers;

    private final List<HandlerMapping> handlerMappings;

    private final PluginManager pluginManager;

    private final TemplateStorage templateStorage;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return notFilterUrls.contains(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest servletRequest,
                                    @NotNull HttpServletResponse servletResponse,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // 构造插件调用链
            List<PluginConfig> plugins = templateStorage.getPlugins(servletRequest.getRequestURI());
            PluginChain pluginChain = pluginManager.createPluginChain(plugins, servletRequest, servletResponse, filterChain);

            // 创建请求对象
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            StreamUtils.copy(servletRequest.getInputStream(), bos);
            Request req = new Request(collectRequestHeader(servletRequest), bos.toByteArray());

            // 创建响应对象
            Response resp = new Response();

            // 插件调用
            pluginChain.doProcess(req, resp);

            // http状态码写回
            HttpStatus status = Optional.ofNullable(resp.getStatus()).orElse(HttpStatus.OK);
            servletResponse.setStatus(status.value());

            // 请求头写回
            Map<String, String> respHeaders = resp.getHeaders();
            respHeaders.forEach(servletResponse::addHeader);

            // 响应内容写回
            byte[] bytes = Optional.ofNullable(resp.getBody()).orElse(new byte[0]);
            StreamUtils.copy(bytes, servletResponse.getOutputStream());
        } catch (Exception e) {
            processExceptionWithGlobalExceptionHandler(servletRequest, servletResponse, e);
        }
    }

    /**
     * 使用全局异常处理器来处理异常
     *
     * @param request  请求
     * @param response 响应
     * @param e        异常
     */
    private void processExceptionWithGlobalExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException {
        HandlerExecutionChain handler = null;
        if (!CollectionUtils.isEmpty(this.handlerMappings)) {
            for (HandlerMapping mapping : this.handlerMappings) {
                try {
                    handler = mapping.getHandler(request);
                } catch (Exception ex) {
                    throw new ServletException(ex);
                }
                if (handler != null) {
                    break;
                }
            }
        }

        if (handler == null) return;
        if (!CollectionUtils.isEmpty(this.handlerExceptionResolvers)) {
            for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
                if (resolver.resolveException(request, response, handler.getHandler(), e) != null) {
                    break;
                }
            }
        }
    }

    private Map<String, String> collectRequestHeader(@NotNull HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            headers.put(key, request.getHeader(key));
        }
        return headers;
    }
}
