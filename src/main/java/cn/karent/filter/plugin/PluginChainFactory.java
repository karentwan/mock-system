package cn.karent.filter.plugin;

import cn.karent.common.Configurable;
import cn.karent.core.model.PluginConfig;
import cn.karent.core.storage.TemplateStorage;
import cn.karent.filter.plugin.parse.PostParsePlugin;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.SmartValidator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wanshengdao
 * @date 2024/6/16
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class PluginChainFactory {

    private final SmartValidator validator;

    private final TemplateStorage templateStorage;

    private final BeanFactory beanFactory;

    private final Map<String, Node> cache = new ConcurrentHashMap<>(16);

    private final Node DEFAULT = new Node(0L, null);

    /**
     * 构建插件调用链
     *
     * @param pluginConfigs 插件配置列表
     * @return 插件调用链
     */
    private List<Plugin> constructPluginList(List<PluginConfig> pluginConfigs) {
        List<Plugin> result = new ArrayList<>();
        // 加入默认的请求体解析插件
        result.add(beanFactory.getBean(PostParsePlugin.BEAN_NAME, Plugin.class));
        if (!ObjectUtils.isEmpty(pluginConfigs)) {
            pluginConfigs.forEach(k -> {
                String name = k.getName();
                Plugin plugin = beanFactory.getBean(name, Plugin.class);
                if (ObjectUtils.isEmpty(plugin)) {
                    log.info("该插件未曾实现: {}", name);
                    return;
                }
                result.add(plugin);
                // 解析配置文件的配置
                if (StringUtils.isBlank(k.getConfig())) {
                    return;
                }
                if (plugin instanceof Configurable<?> configurable) {
                    configurable.configure(k.getConfig(), validator);
                }
            });
        }
        return result;
    }

    /**
     * 创建插件调用链
     *
     * @param request  请求
     * @param response 响应
     * @param chain    过滤器链
     * @return 插件调用链
     */
    public PluginChain createPluginChain(HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain chain) throws IOException {
        String api = request.getRequestURI();
        Long newestTimestamp = templateStorage.getTimestamp(api);
        Node node = cache.getOrDefault(api, DEFAULT);
        PluginChain pluginChain = new PluginChain(request, response, chain);
        List<Plugin> plugins = null;
        if (node.getTimestamp().compareTo(newestTimestamp) == 0) {
            plugins = node.getPlugins();
        } else {
            log.debug("脏页, 重新构建插件调用链, 最新时间戳: {}\t缓存时间戳: {}", newestTimestamp, node.getTimestamp());
            plugins = constructPluginList(templateStorage.getPlugins(api));
            cache.put(api, new Node(newestTimestamp, plugins));
        }
        plugins.forEach(pluginChain::addPlugin);
        return pluginChain;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class Node {

        private Long timestamp;

        private List<Plugin> plugins;

    }


}
