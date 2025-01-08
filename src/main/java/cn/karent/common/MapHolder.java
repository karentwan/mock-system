package cn.karent.common;

import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author wanshengdao
 * @date 2025/1/8
 */
@RequiredArgsConstructor
public class MapHolder {

    private final Map<String, Object> data;

    private List<String> keys(String key) {
        key = key.trim();
        if (key.startsWith("${") && key.endsWith("}")) {
            key = key.substring(2, key.length()-1);
        }
        String[] split = key.split("\\.");
        return Arrays.asList(split);
    }

    @SuppressWarnings("unchecked")
    private <T> T get(Map<String, Object> data, List<String> keys, int idx, Class<T> clazz) {
        String key = keys.get(idx);
        Object obj = data.get(key);
        if (idx >= keys.size()) {
            return (T) obj;
        }
        if (obj instanceof Map) {
            return get((Map<String, Object>) obj, keys, idx+1, clazz);
        }
        throw new IllegalStateException("key不正确");
    }

    public String getString(String key) {
        return get(this.data, keys(key), 0, String.class);
    }

    public int getInt(String key) {
        return get(this.data, keys(key), 0, Integer.class);
    }


}
