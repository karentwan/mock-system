package cn.karent.core.render;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储配置的模板
 *
 * @author wanshengdao
 * @date 2024/6/14
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class TemplateStorage {

    @Setter
    @Getter
    @Slf4j
    static class Entry<T> {

        private Map<String, String> headers;

        private T template;

        public Entry(Map<String, String> headers, T template) {
            this.headers = headers;
            this.template = template;
        }
    }


    private final Map<String, Entry<String>> templates = new ConcurrentHashMap<>(16);

    private final Map<String, Entry<Template>> cache = new ConcurrentHashMap<>(16);

    private final Configuration configuration;

    /**
     * 获取模板
     *
     * @param api 接口名称
     * @return 模板
     * @throws IOException
     */
    @SuppressWarnings("all")
    public Entry<Template> getTemplate(String api) throws IOException {
        Entry<Template> template = cache.get(api);
        if (template == null) {
            synchronized (this) {
                if (template == null) {
                    Entry<String> entry = getTpl(api);
                    Assert.notNull(entry, "未配置该模板");
                    Template tpl = new Template(api, entry.getTemplate(), configuration);
                    template = new Entry<>(entry.getHeaders(), tpl);
                    cache.put(api, template);
                }
            }
        }
        return template;
    }

    /**
     * 存储模板
     *
     * @param api      接口api
     * @param headers  http响应头
     * @param template 模板内容
     */
    public void store(String api, Map<String, String> headers, String template) {
        Entry<String> entry = new Entry<>(headers, template);
        templates.put(api, entry);
        cache.remove(api);
    }

    /**
     * 获取末班
     *
     * @param api 接口名称
     * @return 模板
     */
    @Nullable
    private Entry<String> getTpl(String api) {
        return templates.get(api);
    }

}
