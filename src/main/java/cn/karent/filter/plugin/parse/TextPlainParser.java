package cn.karent.filter.plugin.parse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

/**
 * 一些文本文件的支持
 *
 * @author wanshengdao
 * @date 2024/6/18
 */
public class TextPlainParser implements Parser {

    @Override
    public boolean support(String contentType) {
        return StringUtils.contains(contentType, MediaType.TEXT_HTML_VALUE)
                || StringUtils.contains(contentType, MediaType.TEXT_PLAIN_VALUE);
    }

    @Override
    public byte[] parse(String contentType, byte[] src) {
        return new byte[0];
    }
}
