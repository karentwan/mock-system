package cn.karent.filter.plugin;

import cn.karent.common.ServletInputStreamAdapter;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author wanshengdao
 * @date 2024/6/16
 */
public class BodyHttpServletRequestAdapter extends HttpServletRequestWrapper {

    private final ServletInputStream servletInputStream;

    private final String api;

    public BodyHttpServletRequestAdapter(HttpServletRequest request, String api, byte[] content) {
        super(request);
        this.api = api;
        servletInputStream = new ServletInputStreamAdapter(content);
    }

    @Override
    public String getRequestURI() {
        return StringUtils.isBlank(api) ? super.getRequestURI() : api;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(servletInputStream, StandardCharsets.UTF_8));
    }
}
