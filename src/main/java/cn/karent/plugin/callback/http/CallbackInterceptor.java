package cn.karent.plugin.callback.http;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 该接口是回调插件的拦截器标示
 *
 * @author wanshengdao
 * @date 2024/6/29
 */
public interface CallbackInterceptor {

    /**
     * RestTemplate拦截器
     *
     * @param request   要发送的http请求
     * @param body      要发送的请求体
     * @param extra     拦截器可以使用的额外数据, 例如加验签
     * @param execution 执行链
     * @return 响应
     * @throws IOException 异常
     */
    ClientHttpResponse intercept(HttpRequest request, byte[] body, Map<String, Object> extra, ClientHttpRequestExecution execution) throws IOException;

}
