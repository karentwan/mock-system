package cn.karent.plugin.callback.http;

import cn.karent.plugin.callback.ConfiguredRequest;
import cn.karent.util.JsonUtils;
import lombok.RequiredArgsConstructor;
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
public class Interceptor implements ClientHttpRequestInterceptor {

    public static final String INTERCEPTOR_NAME = "name";

    private final Map<String, CallbackInterceptor> interceptorMap;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String s = new String(body, StandardCharsets.UTF_8);
        ConfiguredRequest configuredRequest = JsonUtils.parseObject(s, ConfiguredRequest.class);
        Map<String, Object> config = configuredRequest.getConfig();
        byte[] bytes = JsonUtils.toBytes(configuredRequest.getBody());
        String name = (String) config.get(INTERCEPTOR_NAME);
        CallbackInterceptor callbackInterceptor = interceptorMap.get(name);
        if (!Objects.isNull(callbackInterceptor)) {
            return callbackInterceptor.intercept(request, bytes, execution);
        } else {
            return execution.execute(request, bytes);
        }
    }
}
