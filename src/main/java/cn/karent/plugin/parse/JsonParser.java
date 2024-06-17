package cn.karent.plugin.parse;


import org.springframework.http.MediaType;

/**
 * @author wanshengdao
 * @date 2024/6/17
 */
public class JsonParser implements Parser{

    @Override
    public boolean match(String contentType) {
        return MediaType.APPLICATION_JSON_VALUE.equals(contentType)
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType);
    }

    @Override
    public byte[] parse(String contentType, byte[] src) {
        return src;
    }
}
