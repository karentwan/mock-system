package cn.karent.plugin;

import cn.karent.common.PluginComponent;
import cn.karent.filter.plugin.ConfigurablePlugin;
import cn.karent.filter.plugin.Response;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 更改响应的http状态码
 *
 * @author wanshengdao
 * @date 2024/6/17
 */
@Slf4j
@PluginComponent(HttpStatusPlugin.BEAN_NAME)
public class HttpStatusPlugin extends ConfigurablePlugin<HttpStatusPlugin.Config> {

    public static final String BEAN_NAME = "HttpStatus";

    private HttpStatus getHttpStatusFrom(int status) {
        return Stream.of(HttpStatus.values())
                .filter(item -> item.value() == status)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的响应码"));
    }

    @Override
    protected void processResponse(Response response) {
        if (Objects.isNull(config)) {
            log.warn("未配置http状态码, 该插件不起作用");
            return;
        }
        response.setStatus(getHttpStatusFrom(config.getStatus()));
    }


    @Getter
    @Setter
    @Validated
    public static class Config {

        @Positive
        @NotNull
        private Integer status;

    }

}
