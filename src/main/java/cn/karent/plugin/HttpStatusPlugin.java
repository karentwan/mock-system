package cn.karent.plugin;

import cn.karent.filter.plugin.core.Configurable;
import cn.karent.filter.plugin.core.PluginAdapter;
import cn.karent.filter.plugin.core.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 更改响应的http状态码
 *
 * @author wanshengdao
 * @date 2024/6/17
 */
@Slf4j
@Component(HttpStatusPlugin.BEAN_NAME)
public class HttpStatusPlugin extends PluginAdapter implements Configurable<HttpStatusPlugin.Config> {

    public static final String BEAN_NAME = "HttpStatus";

    private Config config;

    @Override
    public void configure0(Config config) {
        this.config = config;
    }

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
    public static class Config {

        private int status;

    }

}
