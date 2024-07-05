package cn.karent.core.render;

import cn.karent.exception.RenderException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class TemplateRender {

    public String renderContent(Template template, Map<String, Object> dataModel) {
        try {
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RenderException(e);
        }
    }

}
