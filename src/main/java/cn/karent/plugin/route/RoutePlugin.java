package cn.karent.plugin.route;

import cn.karent.common.DataModelUtils;
import cn.karent.common.PluginComponent;
import cn.karent.filter.plugin.ConfigurablePlugin;
import cn.karent.filter.plugin.Request;
import cn.karent.plugin.route.predicate.PredicateFactory;
import cn.karent.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 路由插件
 *
 * @author wanshengdao
 * @date 2025/1/8
 */
@PluginComponent(RoutePlugin.NAME)
public class RoutePlugin extends ConfigurablePlugin<RoutePlugin.Config> {

    public static final String NAME = "Route";

    private Map<String, Object> createDataModel(Request request) {
        Map<String, Object> headers = new HashMap<>();
        request.iterator().forEachRemaining(item -> headers.put(item.getName(), item.getValue()));
        Map<String, Object> body = JsonUtils.parseMap(new String(request.getBody()));
        return DataModelUtils.createDataModel(headers, body);
    }

    @Override
    protected void processRequest(Request request) {
        Map<String, Object> dataModel = createDataModel(request);
        // 构建谓词列表
        List<Route> routes = config.getRoutes();
        for (Route route : routes) {
            Predicate<Map<String, Object>> p = PredicateFactory.createPredicateComposite(route.getPredicates());
            // 匹配上路由后, 修改路径
            if (p.test(dataModel)) {
                request.setApi(route.getPath());
                break;
            }
        }
    }

    @Getter
    @Setter
    public static class PredicateArg {

        private String name;

        private Map<String, Object> args;

    }

    @Getter
    @Setter
    public static class Route {

        private String path;

        private List<PredicateArg> predicates;

    }

    @Getter
    @Setter
    public static class Config {

        private List<Route> routes;

    }

}
