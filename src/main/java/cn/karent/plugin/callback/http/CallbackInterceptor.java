package cn.karent.plugin.callback.http;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;

/**
 * 该接口是回调插件的拦截器标示
 *
 * @author wanshengdao
 * @date 2024/6/29
 */
public interface CallbackInterceptor {

    ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException;

}
