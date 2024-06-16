package cn.karent.filter.log;

import cn.karent.common.ServletInputStreamAdapter;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author wanshengdao
 * @date 2024/6/13
 */
public class BodyCachingRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    private final ServletInputStream inputStream;

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
        return new ServletInputStreamAdapter(cache);
    }
}
