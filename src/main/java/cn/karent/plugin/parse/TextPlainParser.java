package cn.karent.plugin.parse;

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
        return MediaType.TEXT_HTML_VALUE.equals(contentType)
                || MediaType.TEXT_PLAIN_VALUE.equals(contentType);
    }

    @Override
    public byte[] parse(String contentType, byte[] src) {
        return new byte[0];
    }
}
