package cn.karent.core;

import cn.karent.common.Result;
import cn.karent.common.TemplateConfig;
import cn.karent.core.cmd.ConfigCmd;
import cn.karent.core.render.TemplateStorage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanshengdao
 * @date 2024/6/14
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ConfigController {

    private final TemplateConfig.Config templateConfig;

    private final TemplateStorage templateStorage;

    /**
     * 配置模板
     * @return 配置
     */
    @PostMapping("/template/config")
    public Result<String> config(@Valid @RequestBody ConfigCmd cmd) {
        Assert.isTrue(templateConfig.isStringMode(), "非字符串模式下不能配置模板");
        templateStorage.store(cmd.getApi(), cmd.getTemplate());
        return Result.ok();
    }

}
