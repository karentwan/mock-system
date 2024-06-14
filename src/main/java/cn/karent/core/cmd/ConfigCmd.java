package cn.karent.core.cmd;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
     * 模板
     */
    @NotBlank
    private String template;

}
