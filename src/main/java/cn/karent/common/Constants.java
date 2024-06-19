package cn.karent.common;

import java.util.Map;

public class Constants {

    /**
     * 默认的Http响应头
     */
    public static final Map<String, String> DEFAULT_RESPONSE_HEADER = Map.of("content-type", "application/json");

    /**
     * 默认模板
     */
    public static final String DEFAULT_TEMPLATE = "{\"greeting\": \"honey, did you forget configure template?\"}";
}