package cn.karent.common;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * freemarker模板
 *
 * @author wanshengdao
 * @date 2024/6/13
 */
@Configuration
public class TemplateConfig {


    @Setter
    @Getter
    @Configuration
    @ConfigurationProperties(prefix = "template")
    @Validated
    public static class Config {

        @NotNull
        private TemplateLoadMode mode;

        /**
         * 如果是文件模式, 这里是保存的模板路径
         */
        private String templatePath;

        /**
         * 是否是字符串模式
         *
         * @return true- string模式
         */
        public boolean isStringMode() {
            return TemplateLoadMode.STRING.equals(mode);
        }

    }


    @Bean
    public freemarker.template.Configuration configuration(Config config) throws IOException {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_33);
        if (TemplateLoadMode.FILE.equals(config.getMode())) {
            Assert.isTrue(StringUtils.isNotBlank(config.getTemplatePath()), "文件模式下模板路径不能为空");
            configuration.setTemplateLoader(new FileTemplateLoader(new File(config.getTemplatePath())));
        } else {
            configuration.setTemplateLoader(new StringTemplateLoader());
        }
        configuration.setEncoding(Locale.CHINA, "UTF-8");
        return configuration;
    }

}
