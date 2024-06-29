package cn.karent.plugin.callback.interceptor;

import cn.karent.plugin.callback.CallbackInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 微信加签拦截器
 *
 * @author wanshengdao
 * @date 2024/6/29
 */
@RequiredArgsConstructor
@Slf4j
@Component(WeXinSignInterceptor.BEAN_NAME)
public class WeXinSignInterceptor implements ClientHttpRequestInterceptor, CallbackInterceptor {

    public static final String BEAN_NAME = "WeXinSignInterceptor";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        log.info("微信回调, 微信加签");
        return execution.execute(request, body);
    }
}
