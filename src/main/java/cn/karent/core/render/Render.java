package cn.karent.core.render;

import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/14
 */
public interface Render {


    /**
     * 渲染http响应头
     *
     * @return 响应头
     */
    Map<String, String> renderHeader(String api);

    /**
     * 渲染响应内容
     *
     * @param dataModel 数据模型
     * @return 渲染结果
     */
    String renderContent(String api, Map<String, Object> dataModel);

}
