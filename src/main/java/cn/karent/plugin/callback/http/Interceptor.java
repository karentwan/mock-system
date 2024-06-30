package cn.karent.plugin.callback.http;

import cn.karent.plugin.callback.ConfiguredRequest;
import cn.karent.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @author wanshengdao
 * @date 2024/6/30
 */
@RequiredArgsConstructor
@Slf4j
public class Interceptor implements ClientHttpRequestInterceptor {

    public static final String INTERCEPTOR_NAME = "name";

    private final Map<String, CallbackInterceptor> interceptorMap;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        log.debug("开始restTemplate拦截器处理");
        String s = new String(body, StandardCharsets.UTF_8);
        ConfiguredRequest configuredRequest = JsonUtils.parseObject(s, ConfiguredRequest.class);
        Map<String, Object> config = configuredRequest.getConfig();
        Object content = configuredRequest.getBody();
        byte[] bytes = null;
        if (content instanceof String c) {
            bytes = c.getBytes(StandardCharsets.UTF_8);
        } else {
            bytes = JsonUtils.toBytes(content);
        }
        if (Objects.isNull(config)) {
            return execution.execute(request, bytes);
        }
        String name = (String) config.get(INTERCEPTOR_NAME);
        CallbackInterceptor callbackInterceptor = interceptorMap.get(name);
        if (!Objects.isNull(callbackInterceptor)) {
            return callbackInterceptor.intercept(request, bytes, execution);
        } else {
            return execution.execute(request, bytes);
        }
    }
}
