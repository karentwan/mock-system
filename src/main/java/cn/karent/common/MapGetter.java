package cn.karent.common;

import java.util.Collections;
import java.util.Map;

/**
 * Map取值器
 *
 * @author wanshengdao
 * @date 2025/1/19
 */
public class MapGetter {

    private final Map<String, Object> data;

    private static final MapGetter EMPTY = new MapGetter(Collections.emptyMap());

    private MapGetter(Map<String, Object> data) {
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public MapGetter get(String key) {
        if (data.containsKey(key)) {
            return of((Map<String, Object>) data.get(key));
        }
        return EMPTY;
    }

    public String getString(String key) {
        return (String) data.get(key);
    }

    public Object getObject(String key) {
        return data.get(key);
    }

    public static MapGetter of(Map<String, Object> data) {
        return new MapGetter(data);
    }

}
