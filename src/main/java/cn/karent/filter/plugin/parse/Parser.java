package cn.karent.filter.plugin.parse;

/**
 * 请求体解析接口
 *
 * @author wanshengdao
 * @date 2024/6/17
 */
public interface Parser {

    /**
     * 解析器是否支持该内容
     *
     * @param contentType 请求的格式
     * @return true/false
     */
    boolean support(String contentType);


    /**
     * 解析成键值对格式的字节
     *
     * @param contentType 内容类型
     * @param src         请求体
     * @return 键值对
     */
    byte[] parse(String contentType, byte[] src);

}
