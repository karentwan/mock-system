package cn.karent.common;

import cn.karent.util.JsonUtils;
import java.util.Map;

public class Constants {

    /**
     * 默认的Http响应头
     */
    public static final Map<String, String> DEFAULT_RESPONSE_HEADER = Map.of("content-type", "application/json");

    /**
     * 默认模板
     */
    public static final String DEFAULT_TEMPLATE = JsonUtils.toString(
            Map.of("greeting", "honey, did you forget configure template?")
    );
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
    public static final String NOT_FOUND = "404";
}