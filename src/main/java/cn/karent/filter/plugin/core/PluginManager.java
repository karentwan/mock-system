package cn.karent.filter.plugin.core;

import cn.karent.core.model.PluginConfig;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/16
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class PluginManager {

    private final Map<String, Plugin> plugins;

    /**
     * 创建插件调用链
     *
     * @param pluginConfigs 插件配置列表
     * @param request 请求
     * @param response 响应
     * @param chain 过滤器链
     * @return 插件调用链
     */
    public PluginChain createPluginChain(List<PluginConfig> pluginConfigs,
                                         HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain chain) {
        PluginChain pluginChain = new PluginChain(request, response, chain);
        if (!ObjectUtils.isEmpty(pluginConfigs)) {
            pluginConfigs.forEach(k -> {
                String name = k.getName();
                Plugin plugin = plugins.get(name);
                if (ObjectUtils.isEmpty(plugin)) {
                    return;
                }
                pluginChain.addPlugin(plugin);
                // 解析配置文件的配置
                if (StringUtils.isBlank(k.getConfig())) {
                    return;
                }
                if (plugin instanceof Configurable<?> configurable) {
                    configurable.configure(k.getConfig());
                }
            });
        }
        return pluginChain;
    }


}
