package cn.karent.core;

import cn.karent.core.model.Response;
import cn.karent.util.JsonUtils;
import freemarker.template.TemplateException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理mock接口响应的控制器
 *
 * @author wanshengdao
 * @date 2024/6/13
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiController {

    private final TemplateService templateService;

    /**
     * mock接口响应
     *
     * @param headers  http请求头
     * @param request  http请求
     * @param response http响应
     * @throws IOException io异常
     */
    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
    public void mockResponse(@RequestHeader Map<String, Object> headers,
                             HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getRequestURI();
        Map<String, Object> body = new HashMap<>();
        // POST请求获取请求体
        if (RequestMethod.POST.name().equals(request.getMethod())) {
            ServletInputStream is = request.getInputStream();
            String str = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(str)) {
                body = JsonUtils.parseMap(str);
            }
            // GET请求获取地址栏上的参数
        } else {
            Enumeration<String> keys = request.getParameterNames();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                body.put(key, request.getParameter(key));
            }
        }
        Response respStr = templateService.render(path, headers, body);
        respStr.getHeaders().forEach(response::addHeader);
        StreamUtils.copy(respStr.getBody().getBytes(), response.getOutputStream());
    }

}
