package cn.karent.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * @author wanshengdao
 * @date 2024/7/5
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class TemplateFactory {

    private final Configuration configuration;

    /**
     * 创建模板
     *
     * @param name       模板名称
     * @param sourceCode 模板内容
     * @return 模板
     */
    public Template createTemplate(String name, String sourceCode) throws IOException {
        return new Template(name, sourceCode, configuration);
    }

}
