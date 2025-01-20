package cn.karent.core.cmd;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 配置请求
 *
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
public class ConfigCmd {

    /**
     * 接口名称
     */
    @NotBlank
    private String api;

    /**
     * 响应头
     */
    private Map<String, String> headers;

    /**
     * 模板响应
     */
    private Object template;

    /**
     * 插件
     */
    private List<PluginConfig> plugins;

    /**
     * 路由, 当有路由参数时, 将忽略template和plugins的取值
     */
    private List<RouteCmd> routes;

}
