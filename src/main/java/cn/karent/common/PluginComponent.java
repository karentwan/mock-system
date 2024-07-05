package cn.karent.common;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import java.lang.annotation.*;

/**
 * 插件组件, 插件组件是多例的形式
 *
 * @author wanshengdao
 * @date 2024/7/1
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Documented
@Component
public @interface PluginComponent {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
