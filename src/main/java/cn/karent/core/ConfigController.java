package cn.karent.core;

import cn.karent.common.Constants;
import cn.karent.core.cmd.PluginConfig;
import cn.karent.core.cmd.RouteCmd;
import cn.karent.core.storage.TemplateStorage;
import cn.karent.common.Result;
import cn.karent.common.TemplateConfig;
import cn.karent.core.cmd.ConfigCmd;
import cn.karent.plugin.route.RoutePlugin;
import cn.karent.util.JsonUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wanshengdao
 * @date 2024/6/14
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ConfigController {

    public static final String CONFIG_URL = "/config";

    private final TemplateConfig.Config templateConfig;

    private final TemplateStorage templateStorage;

    /**
     * 配置模板
     *
     * @return 配置
     */
    @PostMapping(CONFIG_URL)
    public Result<String> config(@Valid @RequestBody ConfigCmd cmd) {
        Assert.isTrue(templateConfig.isMemoryMode(), "非字符串模式下不能配置模板");
        Map<String, String> headers = Optional.ofNullable(cmd.getHeaders()).orElse(Constants.DEFAULT_RESPONSE_HEADER);
        if (CollectionUtils.isEmpty(cmd.getRoutes())) {
            buildTemplate(cmd.getApi(), headers, cmd.getTemplate(), cmd.getPlugins());
            // 路由模式
        } else {
            List<Map<String, Object>> routeConfig = new ArrayList<>();
            // 注册路由模板
            List<RouteCmd> routes = cmd.getRoutes();
            for (RouteCmd route : routes) {
                String routeApi = cmd.getApi() + "/" + route.getId();
                log.info("注册路由路径: {}", routeApi);
                buildTemplate(routeApi, headers, route.getTemplate(), route.getPlugins());
                routeConfig.add(
                        Map.of("path", routeApi,
                                "template", route.getTemplate(),
                                "predicates", route.getPredicates())
                );
            }
            // 注册母版
            buildTemplate(cmd.getApi(),
                    headers,
                    "404",
                    List.of(PluginConfig.builder()
                            .name(RoutePlugin.NAME)
                            .config(Map.of("config", Map.of("routes", routeConfig)))
                            .build()
                    )
            );
        }
        return Result.ok();
    }

    private void buildTemplate(@NotBlank String api, Map<String, String> headers, Object templateObj, List<PluginConfig> pluginList) {
        String template = Optional.ofNullable(templateObj).map(obj -> {
            if (obj instanceof String str) {
                return str;
            } else {
                return JsonUtils.toString(obj);
            }
        }).orElse(Constants.DEFAULT_TEMPLATE);
        List<PluginConfig> plugins = Optional.ofNullable(pluginList).orElse(new ArrayList<>());
        List<cn.karent.core.model.PluginConfig> encodedPlugins = plugins.stream()
                .map(plugin -> {
                    String config = null;
                    if (!Objects.isNull(plugin.getConfig())) {
                        config = JsonUtils.toString(plugin.getConfig());
                    }
                    return new cn.karent.core.model.PluginConfig(plugin.getName(), config);
                })
                .collect(Collectors.toList());
        templateStorage.store(api, headers, template, encodedPlugins);
    }

}
