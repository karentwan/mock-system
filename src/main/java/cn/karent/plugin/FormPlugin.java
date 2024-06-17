package cn.karent.plugin;

import cn.karent.filter.plugin.PluginAdapter;
import cn.karent.filter.plugin.Request;
import cn.karent.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 解析表单格式的请求体
 *
 * @author wanshengdao
 * @date 2024/6/16
 */
@Slf4j
@Component(FormPlugin.BEAN_NAME)
@RequiredArgsConstructor
public class FormPlugin extends PluginAdapter {

    public static final String BEAN_NAME = "Form";

    @Override
    protected void processRequest(Request request) {
        String content = new String(request.getBody(), StandardCharsets.UTF_8);
        log.info("请求体: {}", content);
        Map<String, String> parsed = Stream.of(content.split("&"))
                .map(item -> item.split("="))
                .collect(Collectors.toMap(strings -> strings[0], strings -> strings[1]));
        request.setBody(JsonUtils.toBytes(parsed));
    }

}

