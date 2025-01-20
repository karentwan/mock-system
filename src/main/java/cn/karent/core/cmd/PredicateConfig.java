package cn.karent.core.cmd;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshengdao
 * @date 2024/6/19
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PredicateConfig {

    private String name;

    private Object config;

}
