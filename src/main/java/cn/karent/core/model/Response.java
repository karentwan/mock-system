package cn.karent.core.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/14
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Response {

    /**
     * 模板响应头
     */
    private Map<String, String> headers;

    /**
     * 模板响应体
     */
    private String body;

}
