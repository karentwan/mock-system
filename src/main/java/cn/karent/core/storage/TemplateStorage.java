package cn.karent.core.storage;

import cn.karent.core.model.PluginConfig;
import freemarker.template.Template;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/17
 */
public interface TemplateStorage {

    /**
     * 获取响应头
     *
     * @param api 接口名称
     * @return 响应头
     * @throws IOException
     */
    Map<String, String> getHeaders(String api) throws IOException;

    /**
     * 获取渲染模板
     *
     * @param api 接口名称
     * @return 模板
     */
    Template getTemplate(String api) throws IOException;

    /**
     * 获取插件列表
     *
     * @param api 接口名
     * @return 插件列表
     */
    List<PluginConfig> getPlugins(String api) throws IOException;

    /**
     * 获取模板保存的时间
     *
     * @return 模板保存时间
     */
    Long getTimestamp(String api) throws IOException;

    /**
     * 存储模板
     *
     * @param api      接口名称
     * @param headers  响应头
     * @param template 模板字符串
     * @param plugins  插件列表
     */
    void store(String api, Map<String, String> headers, String template, List<PluginConfig> plugins);
}
