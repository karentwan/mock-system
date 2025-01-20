package cn.karent.plugin.route.predicate;

import cn.karent.plugin.route.RoutePlugin;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 谓词工厂
 *
 * @author wanshengdao
 * @date 2025/1/19
 */
public class PredicateFactory {

    public static Predicate<Map<String, Object>> create(String name, Map<String, Object> args) {
        switch (name) {
            case "body" -> {
                return new BodyPredicate(args);
            }
            default -> {
                throw new IllegalStateException("未知的谓词");
            }
        }
    }

    public static Predicate<Map<String, Object>> createPredicateComposite(List<RoutePlugin.PredicateArg> list) {
        Predicate<Map<String, Object>> p = item -> true;
        list.forEach(item -> {
            p.and(create(item.getName(), item.getArgs()));
        });
        return p;
    }

}
