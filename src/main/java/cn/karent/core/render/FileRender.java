package cn.karent.core.render;

import cn.karent.common.TemplateLoadMode;
import cn.karent.exception.RenderException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * 文件渲染模板
 *
 * @author wanshengdao
 * @date 2024/6/14
 */
@RequiredArgsConstructor
@Slf4j
@Component
@ConditionalOnProperty(prefix = "template", name = "mode", havingValue = "FILE")
public class FileRender implements Render {


    /**
     * 文件后缀
     */
    public static final String FILE_SUFFIX = ".ftl";

    private final Configuration configuration;

    @Override
    public String render(String api, Map<String, Object> dataModel) {
        api = api.substring(1).replaceAll("/", "_");
        String filePath = api + FILE_SUFFIX;
        try {
            Template template = configuration.getTemplate(filePath);
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RenderException(e);
        }
    }
}
