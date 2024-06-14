package cn.karent.core.render;

import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/14
 */
public interface Render {

    /**
     * 渲染
     *
     * @param dataModel 数据模型
     * @return 渲染结果
     */
    String render(String api, Map<String, Object> dataModel);

}
