package cn.karent.core.render;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

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

    private final Map<String, String> templates = new ConcurrentHashMap<>(16);

    /**
     * 存储模板
     *
     * @param api      接口api
     * @param template 模板内容
     */
    public void store(String api, String template) {
        templates.put(api, template);
    }

    /**
     * 获取末班
     *
     * @param api 接口名称
     * @return 模板
     */
    @Nullable
    public String getTpl(String api) {
        return templates.get(api);
    }

}
