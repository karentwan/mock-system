package cn.karent.plugin.route;

import cn.karent.common.DataModelUtils;
import cn.karent.common.PluginComponent;
import cn.karent.core.model.PluginConfig;
import cn.karent.filter.plugin.*;
import cn.karent.plugin.route.predicate.PredicateFactory;
import cn.karent.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
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

    @Autowired
    private PluginChainFactory pluginChainFactory;

    private Map<String, Object> createDataModel(Request request) {
        Map<String, Object> headers = new HashMap<>();
        request.iterator().forEachRemaining(item -> headers.put(item.getName(), item.getValue()));
        Map<String, Object> body = JsonUtils.parseMap(new String(request.getBody()));
        return DataModelUtils.createDataModel(headers, body);
    }

    @Override
    public void doProcess(Request request, Response response, PluginChain chain) throws ServletException, IOException {
        calcRoute(chain, request);
        chain.doProcess(request, response);
    }

    protected void calcRoute(PluginChain chain, Request request) {
        Map<String, Object> dataModel = createDataModel(request);
        // 构建谓词列表
        List<Route> routes = config.getRoutes();
        for (Route route : routes) {
            Predicate<Map<String, Object>> p = PredicateFactory.createPredicateComposite(route.getPredicates());
            // 匹配上路由后, 修改路径
            if (p.test(dataModel)) {
                request.setApi(route.getPath());
                // 添加插件列表
                List<Plugin> plugins = pluginChainFactory.constructPluginList(route.getPlugins());
                plugins.forEach(chain::addPlugin);
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

        private List<PluginConfig> plugins;

    }

    @Getter
    @Setter
    @Validated
    public static class Config {

        @NotEmpty
        private List<Route> routes;

    }

}
