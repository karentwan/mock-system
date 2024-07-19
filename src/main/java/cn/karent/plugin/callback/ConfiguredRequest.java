package cn.karent.plugin.callback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/30
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConfiguredRequest {

    private Object body;

    private Map<String, Object> config;

}
