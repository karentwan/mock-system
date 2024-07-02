package cn.karent.plugin.callback;

import cn.karent.common.Constants;
import cn.karent.common.PluginComponent;
import cn.karent.filter.plugin.ConfigurablePlugin;
import cn.karent.filter.plugin.Request;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 回调插件
 *
 * @author wanshengdao
 * @date 2024/6/17
 */
@Slf4j
@RequiredArgsConstructor
@PluginComponent(CallbackPlugin.BEAN_NAME)
public class CallbackPlugin extends ConfigurablePlugin<CallbackPlugin.Config> {

    public static final String BEAN_NAME = "Callback";

    private final RestTemplate restTemplate;

    private final ScheduledExecutorService scheduled;

    /**
     * 校验参数配置是否有效
     *
     * @return true/false
     */
    private boolean checkConfig() {
        return "get".equalsIgnoreCase(config.getMethod())
                || "post".equalsIgnoreCase(config.getMethod()) && StringUtils.isNotBlank(config.getBody());
    }

    @Override
    protected void processRequest(Request request) {
        if (!checkConfig()) {
            return;
        }
        int intervalTime = Optional.ofNullable(config.getIntervalTime()).orElse(10);
        TimeUnit unit = Optional.ofNullable(config.getUnit()).orElse(TimeUnit.SECONDS);
        scheduled.schedule(() -> {
            if ("get".equalsIgnoreCase(config.getMethod())) {
                log.info("响应结果: {}", restTemplate.getForObject(config.getUrl(), String.class));
            } else {
                HttpHeaders headers = new HttpHeaders();
                Map<String, String> headerMap = Optional.ofNullable(config.getHeaders())
                        .orElse(Constants.DEFAULT_RESPONSE_HEADER);
                headerMap.forEach(headers::add);
                ConfiguredRequest configuredRequest = new ConfiguredRequest(config.getBody(), config.getInterceptor());
                HttpEntity<ConfiguredRequest> requestEntity = new HttpEntity<>(configuredRequest, headers);
                ResponseEntity<String> entity = restTemplate.postForEntity(config.getUrl(), requestEntity, String.class);
                HttpStatusCode statusCode = entity.getStatusCode();
                String body = entity.getBody();
                log.info("status code: {}\tresponse content:{}", statusCode, body);
            }
        }, intervalTime, unit);
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Validated
    public static class Config {

        /**
         * 回调的地址
         */
        @NotBlank
        private String url;

        /**
         * 回调间隔时间
         */
        private Integer intervalTime;

        /**
         * 单位
         */
        private TimeUnit unit;

        /**
         * 以什么方法回调？ GET还是POST
         */
        @NotBlank
        private String method;

        /**
         * http请求头
         */
        private Map<String, String> headers;

        /**
         * 回调请求体
         */
        private String body;

        /**
         * 回调拦截器列表, 可以在拦截器对请求体进行加签和加密
         */
        private Map<String, Object> interceptor;

    }

}
