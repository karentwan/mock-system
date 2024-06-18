package cn.karent.plugin;

import cn.karent.filter.plugin.Configurable;
import cn.karent.filter.plugin.PluginAdapter;
import cn.karent.filter.plugin.Request;
import cn.karent.util.JsonUtils;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 回调插件
 *
 * @author wanshengdao
 * @date 2024/6/17
 */
@RequiredArgsConstructor
@Slf4j
@Component(CallbackPlugin.BEAN_NAME)
public class CallbackPlugin extends PluginAdapter implements Configurable<CallbackPlugin.Config> {

    public static final String BEAN_NAME = "Callback";

    private final RestTemplate restTemplate;

    private final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

    private Config config;

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
                Map<String, Object> params = JsonUtils.parseMap(config.getBody());
                ResponseEntity<String> entity = restTemplate.postForEntity(config.getUrl(), params, String.class);
                HttpStatusCode statusCode = entity.getStatusCode();
                String body = entity.getBody();
                log.info("status code: {}\tresponse content:{}", statusCode, body);
            }
        }, intervalTime, unit);
    }

    @Override
    public void configure0(Config config) {
        this.config = config;
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
         * 回调请求体
         */
        private String body;

    }

}
