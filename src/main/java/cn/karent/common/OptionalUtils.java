package cn.karent.common;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author wanshengdao
 * @date 2024/4/1
 */
public class OptionalUtils {

    /**
     * 创建一个Optional对象，如果t满足predicate条件，则返回Optional.of(t)，否则返回Optional.empty()
     * @param t 要包装的对象
     * @param predicate 谓词, 判断条件
     * @return 返回Optional 对象
     * @param <T> 泛型
     */
    public static <T> Optional<T> ofCond(T t, Predicate<T> predicate) {
        if (predicate.test(t)) {
            return Optional.of(t);
        }
        return Optional.empty();
    }



}
