package cn.karent.filter.plugin.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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

    private Map<String, String> headers;

    private byte[] body;

}
