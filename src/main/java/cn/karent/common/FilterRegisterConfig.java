package cn.karent.common;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ServletRequestPathFilter;

/**
 * 注册Filter
 *
 * @author wanshengdao
 * @date 2024/6/19
 */
@Configuration
public class FilterRegisterConfig {


    @Bean
    public FilterRegistrationBean<ServletRequestPathFilter> servletRequestPathFilter() {
        FilterRegistrationBean<ServletRequestPathFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ServletRequestPathFilter());
        bean.setOrder(0);
        return bean;
    }

}
