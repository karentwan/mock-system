package cn.karent.filter.log;

import java.util.stream.Stream;

/**
 * 日志水平
 * @author wanshengdao
 * @date 2024/11/22
 */
public enum LogLevel {

    /**
     * 打印请求行
     */
    BASE,

    /**
     * 打印请求行，请求体
     */
    BODY,

    /**
     * 打印请求行、请求头、请求体
     */
    FULL
    ;

    public boolean isLogHeader() {
        return FULL.equals(this);
    }

    public boolean isLogBody() {
        return BODY.equals(this) || FULL.equals(this);
    }

    public static LogLevel of(String level) {
        return Stream.of(values())
                .filter(item -> item.name().equalsIgnoreCase(level))
                .findFirst()
                .orElse(LogLevel.BODY);
    }

}
