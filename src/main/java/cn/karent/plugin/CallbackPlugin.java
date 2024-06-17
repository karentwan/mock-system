package cn.karent.plugin;

import cn.karent.filter.plugin.Configurable;
import cn.karent.filter.plugin.PluginAdapter;
import cn.karent.filter.plugin.Request;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Objects;
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

    @Override
    protected void processRequest(Request request) {
        if (Objects.isNull(config)) {
            log.warn("回调配置无效");
            return;
        }
        scheduled.schedule(() -> {
            // TODO 待实现
        }, config.getIntervalTime(), config.getUnit());
    }

    @Override
    public void configure0(Config config) {
        this.config = config;
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Config {

        /**
         * 回调的地址
         */
        private String url;

        /**
         * 回调间隔时间
         */
        private int intervalTime;

        /**
         * 单位
         */
        private TimeUnit unit;

        /**
         * 以什么方法回调？ GET还是POST
         */
        private String method;

        /**
         * 回调请求体
         */
        private String body;

    }

}
