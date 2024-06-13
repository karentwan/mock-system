package cn.karent.core;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/13
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class BusiService {

    /**
     * 模板中请求头的前缀
     */
    public static final String HEADER = "header";

    /**
     * 模板中请求参数的前缀
     */
    public static final String BODY = "body";

    /**
     * 文件后缀
     */
    public static final String FILE_SUFFIX = ".ftl";

    private final Configuration configuration;

    public String render(String api, Map<String, Object> headers, Map<String, Object> body)
            throws IOException, TemplateException {
        String filePath = api + FILE_SUFFIX;
        Template template = configuration.getTemplate(filePath);
        Map<String, Object> map = Map.of(HEADER, headers, BODY, body);
        StringWriter writer = new StringWriter();
        template.process(map, writer);
        return writer.toString();
    }

}
