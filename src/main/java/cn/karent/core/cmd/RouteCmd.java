package cn.karent.core.cmd;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * @author wanshengdao
 * @date 2025/1/19
 */
@Slf4j
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RouteCmd {

    @NotBlank
    private String id;

    private Object template;

    @NotEmpty
    private List<PredicateConfig> predicates;

    private List<PluginConfig> plugins;

}
