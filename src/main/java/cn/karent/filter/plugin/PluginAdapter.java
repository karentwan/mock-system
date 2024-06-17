package cn.karent.filter.plugin;

import jakarta.servlet.ServletException;
import java.io.IOException;

/**
 * 插件适配器
 *
 * @author wanshengdao
 * @date 2024/6/16
 */
public class PluginAdapter implements Plugin {

    @Override
    public void doProcess(Request request, Response response, PluginChain chain) throws ServletException, IOException {
        processRequest(request);
        chain.doProcess(request, response);
        processResponse(response);
    }

    /**
     * 处理请求
     *
     * @param request 请求
     */
    protected void processRequest(Request request) {

    }

    /**
     * 处理响应
     *
     * @param response 响应
     */
    protected void processResponse(Response response) {

    }


}
