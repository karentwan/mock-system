package cn.karent.core;

import cn.karent.util.JsonUtils;
import freemarker.template.TemplateException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
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
 * @author wanshengdao
 * @date 2024/6/13
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ApiController {

    private final BusiService busiService;

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
    public void mockResponse(@RequestHeader Map<String, Object> headers,
                             HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException {
        String path = request.getRequestURI();
        String api = path.substring(1).replaceAll("/", "_");
        Map<String, Object> body = new HashMap<>();
        // POST请求获取请求体
        if (RequestMethod.POST.name().equals(request.getMethod())) {
            ServletInputStream is = request.getInputStream();
            String str = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
            body = JsonUtils.parseMap(str);
        // GET请求获取地址栏上的参数
        } else {
            Enumeration<String> keys = request.getParameterNames();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                body.put(key, request.getParameter(key));
            }
        }
        String respStr = busiService.render(api, headers, body);
        response.setHeader("Content-Type", "application/json");
        ServletOutputStream sos = response.getOutputStream();
        StreamUtils.copy(respStr.getBytes(), sos);
    }

}
