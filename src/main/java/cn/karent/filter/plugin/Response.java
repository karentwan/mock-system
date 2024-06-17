package cn.karent.filter.plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.Objects;

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

    /**
     * 设置http响应头
     *
     * @param name  响应头名称
     * @param value 响应头值
     */
    public void setHeader(String name, String value) {
        if (!Objects.isNull(headers)) {
            headers.put(name, value);
        }
    }

}
