package cn.karent.core;

import cn.karent.core.model.Response;
import cn.karent.core.render.Render;
import freemarker.template.TemplateException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
     * 模板中请求头的前缀
     */
    public static final String HEADER = "header";

    /**
     * 模板中请求参数的前缀
     */
    public static final String BODY = "body";

    /**
     * 模板中可以使用的函数的前缀
     */
    public static final String FUNCTION = "F";


    private final Render render;

    /**
     * 切换
     *
     * @param c    待切换大小写的字符
     * @param mode 要切换成大写还是小写
     * @return 切换完后的字符
     */
    private char toggleCase(char c, ToggleMode mode) {
        char result = c;
        if (ToggleMode.UPPER.equals(mode) && (c >= 'a' && c <= 'z')) {
            result = (char) (c - 'a' + 'A');
        } else if (ToggleMode.LOWER.equals(mode) && (c >= 'A' && c <= 'Z')) {
            result = (char) (c - 'A' + 'a');
        }
        return result;
    }

    /**
     * 转驼峰
     *
     * @param key 待处理的key
     * @return 处理后的key
     */
    private String camel(String key) {
        if (StringUtils.isBlank(key)) return key;
        char[] chs = key.toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(toggleCase(chs[0], ToggleMode.LOWER));
        for (int i = 1, j = 0; i < chs.length; j = i++) {
            if (chs[i] != '-') {
                // 这里表示 -T
                if (chs[j] == '-') {
                    sb.append(toggleCase(chs[i], ToggleMode.UPPER));
                } else {
                    sb.append(chs[i]);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 对header里面的key进行规范化, 转换成驼峰风格
     *
     * @param headers http请求头
     * @return 对所有的key处理后的http请求头
     */
    private Map<String, Object> processKeyOfHeaders(Map<String, Object> headers) {
        Map<String, Object> result = new HashMap<>();
        headers.forEach((k, v) -> {
            result.put(camel(k), v);
        });
        return result;
    }

    /**
     * 渲染模板响应
     *
     * @param api     接口名, 对应模板文件名
     * @param headers http请求头
     * @param body    请求内容
     * @return 响应
     * @throws IOException
     * @throws TemplateException
     */
    public Response render(String api, Map<String, Object> headers, Map<String, Object> body) {
        Map<String, Object> dataModel = Map.of(HEADER, processKeyOfHeaders(headers), BODY, body, FUNCTION, createFunction());
        String content = render.renderContent(api, dataModel);
        Map<String, String> responseHeaders = render.renderHeader(api);
        return Response.builder()
                .headers(responseHeaders)
                .body(content)
                .build();
    }

    /**
     * 模板中可以使用的函数
     *
     * @return 函数集合
     */
    private Map<String, Object> createFunction() {
        return Map.of("random", new Random());
    }


}
