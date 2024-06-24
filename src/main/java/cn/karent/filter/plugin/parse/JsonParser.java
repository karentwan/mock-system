package cn.karent.filter.plugin.parse;


import org.springframework.http.MediaType;

/**
 * @author wanshengdao
 * @date 2024/6/17
 */
public class JsonParser implements Parser{

    @Override
    public boolean support(String contentType) {
        return contentType.startsWith(MediaType.APPLICATION_JSON_VALUE);
    }

    @Override
    public byte[] parse(String contentType, byte[] src) {
        return src;
    }
}
