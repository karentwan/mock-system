package cn.karent.plugin.parse;

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

    @Override
    public boolean match(String contentType) {
        for (Parser parser : parsers) {
            if (parser.match(contentType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public byte[] parse(String contentType, byte[] src) {
        for (Parser parser : parsers) {
            if (parser.match(contentType)) {
                return parser.parse(contentType, src);
            }
        }
        return null;
    }
}
