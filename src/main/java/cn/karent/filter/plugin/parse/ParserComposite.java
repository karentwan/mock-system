package cn.karent.filter.plugin.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanshengdao
 * @date 2024/6/17
 */
public class ParserComposite implements Parser {

    private final List<Parser> parsers = new ArrayList<>(8);

    /**
     * 添加解析器
     *
     * @param parser 解析器
     */
    public void addParser(Parser parser) {
        parsers.add(parser);
    }

    /**
     * 获取解析器
     *
     * @param contentType 消息内容
     * @return 解析器
     */
    private Parser getParser(String contentType) {
        for (Parser parser : parsers) {
            if (parser.support(contentType)) {
                return parser;
            }
        }
        return null;
    }

    @Override
    public boolean support(String contentType) {
        return getParser(contentType) != null;
    }

    @Override
    public byte[] parse(String contentType, byte[] src) {
        Parser parser = getParser(contentType);
        return parser.parse(contentType, src);
    }
}
