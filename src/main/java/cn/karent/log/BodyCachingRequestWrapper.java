package cn.karent.log;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author wanshengdao
 * @date 2024/6/13
 */
public class BodyCachingRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    private ServletInputStream inputStream;

    public BodyCachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = StreamUtils.copyToByteArray(request.getInputStream());
        inputStream = createInputStream(body);
    }

    public byte[] getRequestBody() {
        return body;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return inputStream;
    }

    private ServletInputStream createInputStream(byte[] cache) {
        ByteArrayInputStream bis = new ByteArrayInputStream(cache);
        return new ServletInputStream() {
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

            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }
}
