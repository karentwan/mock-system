package cn.karent.core.render;

import cn.karent.exception.RenderException;
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
 * 字符串渲染模板
 *
 * @author wanshengdao
 * @date 2024/6/14
 */
@RequiredArgsConstructor
@Slf4j
@Component
@ConditionalOnProperty(prefix = "template", name = "mode", havingValue = "STRING")
public class StringRender implements Render {

    private final TemplateStorage templateStorage;

    @Override
    public String renderContent(String api, Map<String, Object> dataModel) {
        try {
            Template template = templateStorage.getTemplate(api);
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RenderException(e);
        }
    }

    @Override
    public Map<String, String> renderHeader(String api) {
        try {
            return templateStorage.getHeaders(api);
        } catch (IOException e) {
            throw new RenderException(e);
        }
    }
}
