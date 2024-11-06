package cn.karent.core.storage;

import cn.karent.common.TemplateFactory;
import cn.karent.core.model.PluginConfig;
import freemarker.template.Template;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wanshengdao
 * @date 2024/6/17
 */
public abstract class AbstractTemplateStorage implements TemplateStorage {

    private final Map<String, Config<Template>> cache = new ConcurrentHashMap<>(16);

    @Autowired
    private TemplateFactory templateFactory;

    /**
     * 获取模板
     *
     * @param api 接口名称
     * @return 模板
     * @throws IOException
     */
    @SuppressWarnings("all")
    private Config<Template> getSavedTemplate(String api) throws IOException {
        Config<Template> template = cache.get(api);
        if (template == null) {
            synchronized (this) {
                if (template == null) {
                    Config<String> config = getSavedTemplate0(api);
                    Assert.notNull(config, "未配置该模板");
                    Template tpl = templateFactory.createTemplate(api, config.getTemplate());
                    template = new Config<>(config.getHeaders(), tpl, config.getPlugins());
                    cache.put(api, template);
                }
            }
        }
        return template;
    }


    /**
     * 获取响应头
     *
     * @param api
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, String> getHeaders(String api) throws IOException {
        return getSavedTemplate(api).getHeaders();
    }

    /**
     * 获取响应模板
     *
     * @param api 接口名称
     * @return 响应模板
     * @throws IOException io异常
     */
    @Override
    public Template getTemplate(String api) throws IOException {
        return getSavedTemplate(api).getTemplate();
    }

    @Override
    public Long getTimestamp(String api) throws IOException {
        return getSavedTemplate(api).getTimestamp();
    }

    /**
     * 获取配置的插件
     *
     * @param api 接口名称
     * @return 插件列表
     * @throws IOException io异常
     */
    @Override
    public List<PluginConfig> getPlugins(String api) throws IOException {
        return getSavedTemplate(api).getPlugins();
    }

    /**
     * 存储模板
     *
     * @param api      接口api
     * @param headers  http响应头
     * @param template 模板内容
     * @param plugins  插件列表
     */
    @Override
    public void store(String api, Map<String, String> headers, String template, List<PluginConfig> plugins) {
        Config<String> config = new Config<>(headers, template, plugins);
        store0(api, config);
        cache.remove(api);
    }

    /**
     * 子类需实现该接口对数据配置信息进行存储
     *
     * @param api    接口名称
     * @param config 配置信息
     */
    protected abstract void store0(String api, Config<String> config);

    /**
     * 获取保存的响应模板
     *
     * @param api 接口名称
     * @return 响应模板
     */
    @Nullable
    protected abstract Config<String> getSavedTemplate0(String api);

    @Setter
    @Getter
    @Slf4j
    protected static class Config<T> {

        /**
         * 响应头
         */
        private Map<String, String> headers;

        /**
         * 插件列表
         */
        private List<PluginConfig> plugins;

        /**
         * 响应模板, 该模板经过freemarker渲染后就是真正的响应内容
         */
        private T template;

        /**
         * 缓存时间戳
         */
        private Long timestamp;

        public Config(Map<String, String> headers, T template, List<PluginConfig> plugins) {
            this.headers = headers;
            this.template = template;
            this.plugins = plugins;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
