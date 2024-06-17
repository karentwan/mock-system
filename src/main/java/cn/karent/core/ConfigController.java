package cn.karent.core;

import cn.karent.common.Constants;
import cn.karent.core.storage.TemplateStorage;
import cn.karent.util.OptionalUtils;
import cn.karent.common.Result;
import cn.karent.common.TemplateConfig;
import cn.karent.core.cmd.ConfigCmd;
import cn.karent.util.JsonUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wanshengdao
 * @date 2024/6/14
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ConfigController {

    public static final String CONFIG_URL = "/template/config";

    private final TemplateConfig.Config templateConfig;

    private final TemplateStorage templateStorage;

    /**
     * 配置模板
     *
     * @return 配置
     */
    @PostMapping(CONFIG_URL)
    public Result<String> config(@Valid @RequestBody ConfigCmd cmd) {
        Assert.isTrue(templateConfig.isMemoryMode(), "非字符串模式下不能配置模板");
        String headerStr = OptionalUtils.ofCond(cmd.getHeaders(), StringUtils::isNotBlank).orElse(Constants.DEFAULT_RESPONSE_HEADER);
        Map<String, Object> headers = JsonUtils.parseMap(headerStr);
        Map<String, String> collect = headers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toString()));
        templateStorage.store(cmd.getApi(), collect, cmd.getTemplate(), cmd.getPlugins());
        return Result.ok();
    }

}
