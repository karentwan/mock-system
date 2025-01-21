package cn.karent.plugin.route.predicate;

import cn.karent.common.Constants;
import cn.karent.common.MapGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.function.Predicate;

/**
 * @author wanshengdao
 * @date 2025/1/21
 */
@RequiredArgsConstructor
public class HeadPredicate implements Predicate<Map<String, Object>> {

    public static final String NAME = "head";

    private final Map<String, Object> args;

    @Override
    public boolean test(Map<String, Object> map) {
        String key = (String) args.get("head");
        Object value = args.get("value");
        MapGetter getter = MapGetter.of(map);
        Object v = getter.get(Constants.HEADER).getObject(key);
        if (v == null) {
            return false;
        }
        if (v instanceof String s && value instanceof String v1) {
            return s.matches(v1);
        }
        return v.equals(value);
    }
}
