package cn.karent.core.storage;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存存储模板
 *
 * @author wanshengdao
 * @date 2024/6/14
 */
@RequiredArgsConstructor
@Slf4j
@Component
@ConditionalOnProperty(prefix = "template", name = "mode", havingValue = "MEM")
public class MemoryTemplateStorage extends AbstractTemplateStorage {


    private final Map<String, Entry<String>> templates = new ConcurrentHashMap<>(16);

    /**
     * 获取末班
     *
     * @param api 接口名称
     * @return 模板
     */
    @Override
    @Nullable
    protected Entry<String> getTpl(String api) {
        return templates.get(api);
    }

    @Override
    protected void store0(String api, Entry<String> entry) {
        templates.put(api, entry);
    }
}
