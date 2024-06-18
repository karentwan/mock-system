package cn.karent.plugin.parse;

import cn.karent.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.*;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.http.MediaType;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表单格式的解析
 * (会忽略文件)
 *
 * @author wanshengdao
 * @date 2024/6/18
 */
@Slf4j
public class MultipartFormParser implements Parser {

    @Override
    public boolean support(String contentType) {
        return contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
    }

    @Override
    public byte[] parse(String contentType, byte[] src) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        FileUpload upload = new FileUpload();
        upload.setFileItemFactory(factory);
        upload.setFileSizeMax(1024);
        upload.setSizeMax(1024);
        upload.setFileCountMax(128);
        UploadContext ctx = new UploadContext() {
            @Override
            public long contentLength() {
                return src.length;
            }

            @Override
            public String getCharacterEncoding() {
                return "utf-8";
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(src);
            }
        };
        try {
            List<FileItem> fileItems = upload.parseRequest(ctx);
            Map<String, Object> content = new HashMap<>(16);
            for (FileItem fileItem : fileItems) {
                // 只处理表单项
                if (fileItem.isFormField()) {
                    content.put(fileItem.getFieldName(), new String(fileItem.get(), StandardCharsets.UTF_8));
                } else {
                    log.debug("discard other content");
                }
            }
            return JsonUtils.toBytes(content);
        } catch (FileUploadException e) {
            log.error("异常: ", e);
            throw new IllegalStateException(e);
        }
    }
}
