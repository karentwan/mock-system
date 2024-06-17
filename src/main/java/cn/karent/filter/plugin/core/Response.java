package cn.karent.filter.plugin.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/16
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Response {

    /**
     * http响应状态码
     */
    private HttpStatus status;

    /**
     * http响应头, 可以更改
     */
    private Map<String, String> headers;

    /**
     * http响应体
     */
    private byte[] body;

}
