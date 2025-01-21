package cn.karent.filter.plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author wanshengdao
 * @date 2024/6/16
 */
@Slf4j
@AllArgsConstructor
public class Request implements Iterable<Request.HeadPair> {

    @Setter
    @Getter
    private String api;

    /**
     * http请求方法
     */
    @Getter
    private String method;

    /**
     * header只读, 不允许修改
     */
    private final Map<String, String> headers;

    @Setter
    @Getter
    private byte[] body;

    /**
     * 获取请求头
     *
     * @param key 请求头名称
     * @return 请求头值
     */
    public String getHeader(String key) {
        return headers.get(key);
    }

    /**
     * 遍历请求头
     *
     * @return 迭代器
     */
    @NotNull
    @Override
    public Iterator<HeadPair> iterator() {
        Set<Map.Entry<String, String>> entries = headers.entrySet();
        Iterator<Map.Entry<String, String>> it = entries.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public HeadPair next() {
                Map.Entry<String, String> entry = it.next();
                return new HeadPair(entry.getKey(), entry.getValue());
            }
        };
    }


    @AllArgsConstructor
    @Getter
    public static class HeadPair {

        private String name;

        private String value;

    }
}
