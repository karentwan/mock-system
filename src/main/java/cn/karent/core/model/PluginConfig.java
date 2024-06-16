package cn.karent.core.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/**
 * @author wanshengdao
 * @date 2024/6/16
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PluginConfig {

    /**
     * 插件名
     */
    private String name;

    /**
     * 插件配置项
     */
    private String config;

}
