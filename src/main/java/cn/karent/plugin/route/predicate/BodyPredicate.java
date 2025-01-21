package cn.karent.plugin.route.predicate;

import cn.karent.common.MapGetter;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author wanshengdao
 * @date 2025/1/8
 */
@RequiredArgsConstructor
public class BodyPredicate implements Predicate<Map<String, Object>> {

    private final Map<String, Object> args;

    @Override
    public boolean test(Map<String, Object> map) {
        String key = (String) args.get("name");
        Object value = args.get("value");
        MapGetter getter = MapGetter.of(map);
        Object v = getter.get("body").getObject(key);
        if (v instanceof String s && value instanceof String v1) {
            return s.matches(v1);
        }
        return v.equals(value);
    }
}
