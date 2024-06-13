package cn.karent.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Locale;

/**
 * freemarker模板
 *
 * @author wanshengdao
 * @date 2024/6/13
 */
@Configuration
public class TemplateConfig {


    @Bean
    public freemarker.template.Configuration configuration() {
        freemarker.template.Configuration config = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_33);
        config.setClassForTemplateLoading(TemplateConfig.class, "/api");
        config.setEncoding(Locale.CHINA, "UTF-8");
        return config;
    }

}
