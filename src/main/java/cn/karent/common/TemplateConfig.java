package cn.karent.common;

import freemarker.cache.StringTemplateLoader;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
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
         * 是否是字符串模式
         *
         * @return true- string模式
         */
        public boolean isStringMode() {
            return TemplateLoadMode.STRING.equals(mode);
        }

    }


    @Bean
    public freemarker.template.Configuration configuration(Config config) {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_33);
        if (TemplateLoadMode.FILE.equals(config.getMode())) {
            configuration.setClassForTemplateLoading(TemplateConfig.class, "/api");
        } else {
            configuration.setTemplateLoader(new StringTemplateLoader());
        }
        configuration.setEncoding(Locale.CHINA, "UTF-8");
        return configuration;
    }

}
