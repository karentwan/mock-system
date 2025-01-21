package cn.karent.plugin.route.predicate;

import cn.karent.common.Constants;
import cn.karent.common.MapGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author wanshengdao
 * @date 2025/1/8
 */
@RequiredArgsConstructor
public class BodyPredicate implements Predicate<Map<String, Object>> {

    public static final String NAME = "body";

    private final Map<String, Object> args;

    private List<String> toKeys(String key) {
        return Arrays.asList(key.split("\\."));
    }

    private Object get(MapGetter getter, List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        }
        int n = keys.size();
        MapGetter body = getter.get(Constants.BODY);
        if (n == 1) {
            return body.getObject(keys.get(0));
        }
        for (int i = 0; i < n - 1; i++) {
            body = body.get(keys.get(i));
        }
        return body.getObject(keys.get(n - 1));
    }

    @Override
    public boolean test(Map<String, Object> map) {
        String key = (String) args.get("name");
        Object value = args.get("value");
        MapGetter getter = MapGetter.of(map);
        List<String> keys = toKeys(key);
        Object v = get(getter, keys);
        if (v == null) {
            return false;
        }
        if (v instanceof String s && value instanceof String v1) {
            return s.matches(v1);
        }
        return v.equals(value);
    }
}
