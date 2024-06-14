package cn.karent.core.render;

import cn.karent.exception.RenderException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private final Map<String, Template> cache = new ConcurrentHashMap<>(16);

    private final Configuration configuration;

    @SuppressWarnings("all")
    private Template getTemplate(String api) throws IOException {
        Template template = cache.get(api);
        if (template == null) {
            synchronized (this) {
                if (template == null) {
                    String tpl = templateStorage.getTpl(api);
                    Assert.notNull(tpl, "未配置该模板");
                    template = new Template(api, tpl, configuration);
                    cache.put(api, template);
                }
            }
        }
        return template;
    }

    @Override
    public String render(String api, Map<String, Object> dataModel) {
        try {
            Template template = getTemplate(api);
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RenderException(e);
        }
    }
}
