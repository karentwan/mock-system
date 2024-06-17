package cn.karent.plugin.parse;

/**
 * 请求体解析接口
 *
 * @author wanshengdao
 * @date 2024/6/17
 */
public interface Parser {

    /**
     * 匹配
     *
     * @param contentType 请求的格式
     * @return true/false
     */
    boolean match(String contentType);


    /**
     * 解析成键值对格式的字节
     *
     * @param contentType
     * @param src         请求体
     * @return 键值对
     */
    byte[] parse(String contentType, byte[] src);

}
