package cn.karent.plugin.parse;

/**
 * @author wanshengdao
 * @date 2024/6/18
 */
public class ParserFactory {


    /**
     * 创建解析器
     *
     * @return 解析器
     */
    public static Parser createParser() {
        ParserComposite composite = new ParserComposite();
        composite.addParser(new JsonParser());
        composite.addParser(new UrlEncodedFormParser());
        composite.addParser(new MultipartFormParser());
        composite.addParser(new TextPlainParser());
        return composite;
    }

}
