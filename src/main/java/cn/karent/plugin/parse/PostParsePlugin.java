package cn.karent.plugin.parse;

import cn.karent.filter.plugin.PluginAdapter;
import cn.karent.filter.plugin.Request;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Post请求体解析插件
 *
 * @author wanshengdao
 * @date 2024/6/16
 */
@Slf4j
@Component(PostParsePlugin.BEAN_NAME)
@RequiredArgsConstructor
public class PostParsePlugin extends PluginAdapter {

    public static final String BEAN_NAME = "PostParse";

    private final Parser parser = ParserFactory.createParser();

    /**
     * 只解析POST请求
     *
     * @param request 请求
     * @return true/false
     */
    private boolean shouldParse(Request request) {
        String method = request.getMethod();
        return "post".equalsIgnoreCase(method);
    }

    private String getContentType(Request request) {
        List<String> keys = List.of("Content-Type", "content-type");
        for (String key : keys) {
            String value = request.getHeader(key);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    @Override
    protected void processRequest(Request request) {
        if (!shouldParse(request)) {
            return;
        }
        String contentType = getContentType(request);
        Assert.isTrue(parser.support(contentType), String.format("系统暂不支持该内容类型: %s", contentType));
        byte[] parse = parser.parse(contentType, request.getBody());
        request.setBody(parse);
    }

}

