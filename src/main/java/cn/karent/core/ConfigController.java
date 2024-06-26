package cn.karent.core;

import cn.karent.common.Constants;
import cn.karent.core.cmd.PluginConfig;
import cn.karent.core.storage.TemplateStorage;
import cn.karent.common.Result;
import cn.karent.common.TemplateConfig;
import cn.karent.core.cmd.ConfigCmd;
import cn.karent.util.JsonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wanshengdao
 * @date 2024/6/14
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ConfigController {

    public static final String CONFIG_URL = "/template/config";

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
        String template = Optional.ofNullable(cmd.getTemplate()).map(obj -> {
            if (obj instanceof String str) {
                return str;
            } else {
                return JsonUtils.toString(obj);
            }
        }).orElse(Constants.DEFAULT_TEMPLATE);
        List<PluginConfig> plugins = Optional.ofNullable(cmd.getPlugins()).orElse(new ArrayList<>());
        List<cn.karent.core.model.PluginConfig> encodedPlugins = plugins.stream()
                .map(plugin -> {
                    String config = null;
                    if (!Objects.isNull(plugin.getConfig())) {
                        config = JsonUtils.toString(plugin.getConfig());
                    }
                    return new cn.karent.core.model.PluginConfig(plugin.getName(), config);
                })
                .collect(Collectors.toList());
        templateStorage.store(cmd.getApi(), headers, template, encodedPlugins);
        return Result.ok();
    }

}
