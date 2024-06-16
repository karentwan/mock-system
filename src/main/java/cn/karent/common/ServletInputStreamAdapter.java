package cn.karent.common;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author wanshengdao
 * @date 2024/6/16
 */
public class ServletInputStreamAdapter extends ServletInputStream {

    private final ByteArrayInputStream bis;

    public ServletInputStreamAdapter(byte[] content) {
        bis = new ByteArrayInputStream(content);
    }

    @Override
    public boolean isFinished() {
        return bis.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException("不支持设置ReadListener");
    }

    @Override
    public int read() throws IOException {
        return bis.read();
    }
}
