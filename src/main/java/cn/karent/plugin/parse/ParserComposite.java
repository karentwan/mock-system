package cn.karent.plugin.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanshengdao
 * @date 2024/6/17
 */
public class ParserComposite implements Parser {

    private final List<Parser> parsers = new ArrayList<>(8);

    public ParserComposite() {
        parsers.add(new JsonParser());
        parsers.add(new UrlEncodedFormParser());
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
        return src;
    }
}
