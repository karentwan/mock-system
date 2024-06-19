package cn.karent.filter.plugin;

import cn.karent.util.JsonUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取配置的参数
 *
 * @author wanshengdao
 * @date 2024/6/16
 */
public interface Configurable<C> {

    /**
     * 设置配置信息
     *
     * @param c 反序列化以后得配置类
     */
    void configure0(C c);

    /**
     * 设置配置信息
     *
     * @param str
     * @param validator
     */
    @SuppressWarnings("unchecked")
    default void configure(String str, SmartValidator validator) {
        if (StringUtils.isBlank(str)) {
            return;
        }
        Class<C> clazz = (Class<C>) GenericTypeResolver.resolveTypeArgument(getClass(), Configurable.class);
        C c = JsonUtils.parseObject(str, clazz);
        // 对配置参数进行校验
        if (validator.supports(c.getClass())) {
            BeanPropertyBindingResult bindError = new BeanPropertyBindingResult(c, c.getClass().getSimpleName());
            validator.validate(c, bindError);
            if (bindError.hasErrors()) {
                List<FieldError> fieldErrors = bindError.getFieldErrors();
                String msg = fieldErrors.stream().map(item -> item.getField() + ":" + item.getDefaultMessage()).collect(Collectors.joining(";"));
                throw new IllegalArgumentException(String.format("字段配置错误, 错误信息为: %s", msg));
            }
        }
        configure0(c);
    }
}
