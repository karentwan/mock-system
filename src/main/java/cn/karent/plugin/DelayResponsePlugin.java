package cn.karent.plugin;

import cn.karent.filter.plugin.ConfigurablePlugin;
import cn.karent.filter.plugin.Response;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import java.util.concurrent.TimeUnit;

/**
 * 延时响应插件
 *
 * @author wanshengdao
 * @date 2024/6/18
 */
@Slf4j
@RequiredArgsConstructor
@Component(DelayResponsePlugin.BEAN_NAME)
public class DelayResponsePlugin extends ConfigurablePlugin<DelayResponsePlugin.Config> {

    public static final String BEAN_NAME = "DelayResponse";

    @Override
    protected void processResponse(Response response) {
        try {
            config.getUnit().sleep(config.getDelayTime());
        } catch (InterruptedException e) {
            log.warn("异常: ", e);
        }
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Validated
    public static class Config {


        @Positive
        @NotNull
        private Integer delayTime;

        @NotNull
        private TimeUnit unit;

    }

}
