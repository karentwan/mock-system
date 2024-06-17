package cn.karent.plugin.parse;

import cn.karent.filter.plugin.PluginAdapter;
import cn.karent.filter.plugin.Request;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

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

    public static final String CONTENT_TYPE = "Content-Type";

    private final ParserComposite parserComposite = new ParserComposite();

    /**
     * 是否应该过滤, 只处理POST请求
     *
     * @param request 请求
     * @return true/false
     */
    private boolean shouldFilter(Request request) {
        String method = request.getMethod();
        return !"post".equalsIgnoreCase(method);
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
        if (shouldFilter(request)) {
            return;
        }
        String contentType = getContentType(request);
        if (parserComposite.match(contentType)) {
            byte[] parse = parserComposite.parse(contentType, request.getBody());
            request.setBody(parse);
        }
    }

}

