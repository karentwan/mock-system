package cn.karent.plugin.callback.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/17
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(Map<String, CallbackInterceptor> map) {
        RestTemplate restTemplate = new RestTemplate();
        Interceptor interceptor = new Interceptor(map);
        restTemplate.getInterceptors().add(interceptor);
        return restTemplate;
    }

}
