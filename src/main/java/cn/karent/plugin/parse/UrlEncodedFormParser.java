package cn.karent.plugin.parse;

import cn.karent.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wanshengdao
 * @date 2024/6/17
 */
@Slf4j
public class UrlEncodedFormParser implements Parser{

    @Override
    public boolean match(String contentType) {
        return MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(contentType);
    }

    @Override
    public byte[] parse(String contentType, byte[] src) {
        String content = new String(src, StandardCharsets.UTF_8);
        log.info("请求体: {}", content);
        Map<String, String> parsed = Stream.of(content.split("&"))
                .map(item -> item.split("="))
                .collect(Collectors.toMap(strings -> strings[0], strings -> strings[1]));
        return JsonUtils.toBytes(parsed);
    }
}
