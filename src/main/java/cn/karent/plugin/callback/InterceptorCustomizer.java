package cn.karent.plugin.callback;

import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author wanshengdao
 * @date 2024/6/29
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class InterceptorCustomizer {

    private final Map<String, CallbackInterceptor> interceptors;

    public void customize(RestTemplate restTemplate, List<String> interceptorNames) {
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        // 清空原有拦截器列表
        interceptors.clear();
        interceptorNames = Optional.ofNullable(interceptorNames).orElseGet(ArrayList::new);
        interceptorNames.forEach(name -> {
            CallbackInterceptor callbackInterceptor = this.interceptors.get(name);
            if (callbackInterceptor instanceof ClientHttpRequestInterceptor interceptor) {
                interceptors.add(interceptor);
            } else {
                log.warn("拦截器配置错误: {}", name);
            }
        });
    }

}
