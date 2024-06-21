package cn.karent.plugin;

import cn.karent.filter.plugin.ConfigurablePlugin;
import cn.karent.filter.plugin.Response;
import cn.karent.util.JsonUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 已读乱回
 *
 * @author wanshengdao
 * @date 2024/6/21
 */
@RequiredArgsConstructor
@Slf4j
@Component(RandomResponsePlugin.BEAN_NAME)
public class RandomResponsePlugin extends ConfigurablePlugin<RandomResponsePlugin.Config> {

    public static final String BEAN_NAME = "RandomResponse";

    private void response(Map<String, Object> map, Map<String, Object> result) {
        map.forEach((k, v) -> {
            Object obj = map.get(k);
            if (obj instanceof String) {
                result.put(k, RandomStringUtils.randomAlphabetic(5));
            } else if (obj instanceof Integer) {
                result.put(k, RandomUtils.nextInt(1, 100));
            } else if (obj instanceof Float) {
                result.put(k, RandomUtils.nextFloat(1, 100));
            } else if (obj instanceof Double) {
                result.put(k, RandomUtils.nextDouble(1, 100));
            } else if (obj instanceof Boolean) {
                result.put(k, RandomUtils.nextBoolean());
            } else if (obj instanceof Map cast) {
                Map<String, Object> m = new HashMap<>();
                result.put(k, m);
                response(cast, m);
            }
        });
    }

    @Override
    protected void processResponse(Response resp) {
        String str = new String(resp.getBody(), StandardCharsets.UTF_8);
        if (config.isJson()) {
            Map<String, Object> object = JsonUtils.parseMap(str);
            Map<String, Object> result = new HashMap<>();
            response(object, result);
            resp.setBody(JsonUtils.toBytes(result));
        } else {
            resp.setBody(RandomStringUtils.randomAlphabetic(10).getBytes(StandardCharsets.UTF_8));
        }
    }

    enum Type {
        JSON, STRING
    }

    @Getter
    @Setter
    @Validated
    public static class Config {

        @NotNull
        private Type type;

        public boolean isJson() {
            return Type.JSON.equals(type);
        }

    }

}
