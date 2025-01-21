package cn.karent.util;

import org.springframework.util.ObjectUtils;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 枚举工具类
 *
 * @author wanshengdao
 * @date 2024/6/21
 */
public abstract class EnumUtils {

    /**
     * 根据条件匹配枚举的实例
     *
     * @param clazz     枚举类型
     * @param predicate 谓词
     * @param <E>
     * @return 枚举实例
     */
    public static <E> Optional<E> match(Class<E> clazz, Predicate<E> predicate) {
        E[] constants = clazz.getEnumConstants();
        if (ObjectUtils.isEmpty(constants)) {
            return Optional.empty();
        }
        return Stream.of(constants).filter(predicate).findFirst();
    }

}
