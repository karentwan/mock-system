package cn.karent.filter.plugin.core;

import jakarta.servlet.ServletException;
import java.io.IOException;

/**
 * @author wanshengdao
 * @date 2024/6/16
 */
public interface Plugin {

    /**
     * 插件调用链
     *
     * @param request 请求
     * @param response 响应
     * @param chain 调用链
     */
    void doProcess(Request request, Response response, PluginChain chain) throws ServletException, IOException;

}
