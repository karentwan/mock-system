package cn.karent.core.storage;

import cn.karent.common.Constants;
import cn.karent.common.TemplateConfig;
import cn.karent.core.model.PluginConfig;
import cn.karent.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 文件存储模式
 *
 * @author wanshengdao
 * @date 2024/6/14
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "template", name = "mode", havingValue = "FILE")
public class FileTemplateStorage extends AbstractTemplateStorage {

    public static final String FILE_SUFFIX = ".json";

    public static final String SLASH = "/";

    public static final String HEADERS = "headers";

    public static final String PLUGINS = "plugins";

    public static final String TEMPLATE = "template";

    /**
     * 模板保存的文件夹
     */
    private String templateDirectory;

    @Autowired
    public void setTemplateDirectory(TemplateConfig.Config config) {
        templateDirectory = config.getTemplatePath();
        log.info("初始化模板保存的目录: {}", templateDirectory);
    }

    private String getRealApi(String api) {
        return api.substring(1).replaceAll(SLASH, "_") + FILE_SUFFIX;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> parseHeader(Map<String, Object> map) {
        return (Map<String, String>) map.getOrDefault(HEADERS, Constants.DEFAULT_RESPONSE_HEADER);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private List<PluginConfig> parsePluginConfigs(Map<String, Object> map) {
        if (!map.containsKey(PLUGINS)) {
            return null;
        }
        List<Map<String, Object>> plugins = (List<Map<String, Object>>) map.get(PLUGINS);
        return plugins.stream()
                .map(plugin -> {
                    String config = null;
                    if (plugin.containsKey("config")) {
                        config = JsonUtils.toString(plugin.get("config"));
                    }
                    return new PluginConfig(plugin.get("name").toString(), config);
                })
                .collect(Collectors.toList());
    }

    private String parseTemplate(Map<String, Object> map) {
        Object template = map.get(TEMPLATE);
        return Optional.ofNullable(template).map(t -> {
            if (t instanceof String str) {
                return str;
            } else {
                return JsonUtils.toString(t);
            }
        }).orElse(Constants.DEFAULT_TEMPLATE);
    }

    /**
     * 获取模板
     *
     * @param api 接口名称
     * @return 模板
     */
    @Override
    @Nullable
    protected Config<String> getSavedTemplate0(String api) {
        String path = templateDirectory + SLASH + getRealApi(api);
        log.info("从文件加载配置的模板, path: {}", path);
        File file = new File(path);
        if (!file.exists()) {
            log.debug("文件不存在");
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = fis.readAllBytes();
            Map<String, Object> map = JsonUtils.parseMap(new String(bytes, StandardCharsets.UTF_8));
            Map<String, String> headers = parseHeader(map);
            List<PluginConfig> pluginConfigs = parsePluginConfigs(map);
            String template = parseTemplate(map);
            return new Config<>(headers, template, pluginConfigs);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void store0(String api, Config<String> config) {
        throw new UnsupportedOperationException("不支持的操作");
    }
}
