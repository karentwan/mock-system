package cn.karent.plugin.route;

import cn.karent.common.PluginComponent;
import cn.karent.filter.plugin.ConfigurablePlugin;
import cn.karent.filter.plugin.Request;

import java.util.Map;

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

        return null;
    }

    @Override
    protected void processRequest(Request request) {
        Map<String, Object> dataModel = createDataModel(request);


    }

    public static class Config {

    }

}
