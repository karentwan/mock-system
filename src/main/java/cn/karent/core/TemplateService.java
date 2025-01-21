package cn.karent.core;

import cn.karent.common.DataModelUtils;
import cn.karent.core.model.Response;
import cn.karent.core.render.TemplateRender;
import cn.karent.core.storage.TemplateStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

/**
 * 响应结果渲染
 *
 * @author wanshengdao
 * @date 2024/6/13
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class TemplateService {

    /**
     * 响应渲染器
     */
    private final TemplateRender templateRender;

    /**
     * 模板存储
     */
    private final TemplateStorage templateStorage;

    /**
     * 渲染模板响应
     *
     * @param api     接口名, 对应模板文件名
     * @param headers http请求头
     * @param body    请求内容
     * @return 响应
     */
    public Response render(String api, Map<String, Object> headers, Map<String, Object> body) throws IOException {
        Map<String, Object> dataModel = DataModelUtils.createDataModel(headers, body);
        String content = templateRender.renderContent(templateStorage.getTemplate(api), dataModel);
        Map<String, String> responseHeaders = templateStorage.getHeaders(api);
        return Response.builder()
                .headers(responseHeaders)
                .body(content)
                .build();
    }


}
