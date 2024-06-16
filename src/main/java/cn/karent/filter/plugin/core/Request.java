package cn.karent.filter.plugin.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/16
 */
@Slf4j
@Setter
@Getter
@AllArgsConstructor
public class Request {

    private Map<String, String> headers;

    private byte[] body;

}
