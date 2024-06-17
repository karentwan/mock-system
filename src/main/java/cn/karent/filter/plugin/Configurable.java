package cn.karent.filter.plugin;

import cn.karent.util.JsonUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.core.GenericTypeResolver;

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
     */
    @SuppressWarnings("unchecked")
    default void configure(String str) {
        if (StringUtils.isBlank(str)) {
            return;
        }
        Class<C> clazz = (Class<C>) GenericTypeResolver.resolveTypeArgument(getClass(), Configurable.class);
        C c = JsonUtils.parseObject(str, clazz);
        configure0(c);
    }
}
